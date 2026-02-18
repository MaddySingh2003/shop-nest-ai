from fastapi import FastAPI
import joblib
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity

app =FastAPI()

model = joblib.load("model/price_model.pkl")
le_category = joblib.load("model/le_category.pkl")
le_brand = joblib.load("model/le_brand.pkl")
tfidf_matrix = joblib.load("model/tfidf_matrix.pkl")
vectorizer = joblib.load("model/vectorizer.pkl")
products_df = joblib.load("model/products_df.pkl")


@app.post("/predict-price")
def predict_price(data:dict):
    try:
        category = le_category.transform([data["category"]])[0]
        brand = le_brand.transform([data["brand"]])[0]
        base_price = data["base_price"]
        demand_score = data["demand_score"]
        rating = data["rating"]

        features=np.array([[category,brand,base_price,demand_score,rating]])

        prediction=model.predict(features)[0]

        return{
         "predicted_price": round(float(prediction), 2)
}
    
    except Exception as e:
        return{"error":str(e)}
    

@app.get("/recommend/{product_id}")
def recommend(product_id:int):

    if product_id>=len(products_df):
        return{"error": "Invalid product ID"}
    
    cosine_sim=cosine_similarity(
        tfidf_matrix[product_id],
        tfidf_matrix
    )
    similar_indices=cosine_sim[0].argsort()[-6:][::-1]
    recommendations=[]
    
    for idx in similar_indices:
        if idx != product_id:
            recommendations.append({
                "category": products_df.iloc[idx]["category"],
                "brand": products_df.iloc[idx]["brand"]
            })

    return {"recommendations": recommendations[:5]}

from pydantic import BaseModel

class RecommendationRequest(BaseModel):
    description: str


@app.post("/recommend")
def recommend(data: RecommendationRequest):

    input_vector = vectorizer.transform([data.description])

    cosine_sim = cosine_similarity(
        input_vector,
        tfidf_matrix
    )

    similar_indices = cosine_sim[0].argsort()[-6:][::-1]

    recommendations = []

    for idx in similar_indices:
        recommendations.append({
            "category": products_df.iloc[idx]["category"],
            "brand": products_df.iloc[idx]["brand"]
        })

    return {"recommendations": recommendations[:5]}
