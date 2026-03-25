from fastapi import FastAPI
import joblib
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity
from pydantic import BaseModel

app = FastAPI()

# ================= LOAD MODELS =================
model = joblib.load("model/price_model.pkl")
le_category = joblib.load("model/le_category.pkl")
le_brand = joblib.load("model/le_brand.pkl")

tfidf_matrix = joblib.load("model/tfidf_matrix.pkl")
vectorizer = joblib.load("model/vectorizer.pkl")
products_df = joblib.load("model/products_df.pkl")

# ================= PRICE PREDICTION =================
@app.post("/predict-price")
def predict_price(data: dict):
    try:
        category = 0
        brand = 0

        try:
            category = le_category.transform([data.get("category", "")])[0]
        except:
            pass

        try:
            brand = le_brand.transform([data.get("brand", "")])[0]
        except:
            pass

        base_price = data.get("base_price", 0)
        demand_score = data.get("demand_score", 0.5)
        rating = data.get("rating", 4.0)

        features = np.array([[category, brand, base_price, demand_score, rating]])

        prediction = model.predict(features)[0]

        return {
            "predicted_price": round(float(prediction), 2)
        }

    except Exception as e:
        print("PRICE ERROR:", e)
        return {"predicted_price": base_price}


# ================= RECOMMENDATION (BY ID) =================
@app.get("/recommend/{product_id}")
def recommend_by_id(product_id: int):

    try:
        if product_id >= len(products_df):
            return {"recommendations": []}

        cosine_sim = cosine_similarity(
            tfidf_matrix[product_id],
            tfidf_matrix
        )

        similar_indices = cosine_sim[0].argsort()[::-1][:6]

        recommendations = []

        for idx in similar_indices:
            if idx != product_id:
                product = products_df.iloc[idx]

                recommendations.append({
                    "id": int(product["id"]),
                    "name": str(product["title"]),
                    "price": float(product["price"]),
                    "imageUrl": product.get("thumbnail", "")
                })

        return {"recommendations": recommendations[:5]}

    except Exception as e:
        print("ID RECOMMEND ERROR:", e)
        return {"recommendations": []}


# ================= RECOMMENDATION (BY DESCRIPTION) =================
class RecommendationRequest(BaseModel):
    description: str

@app.post("/recommend")
def recommend(data: RecommendationRequest):

    try:
        description = (data.description or "").lower().strip()

        if not description:
            return {"recommendations": []}

        input_vector = vectorizer.transform([description])
        cosine_sim = cosine_similarity(input_vector, tfidf_matrix)

        similar_indices = cosine_sim[0].argsort()[::-1][:5]

        recommendations = []

        for idx in similar_indices:
            product = products_df.iloc[idx]

            recommendations.append({
                "id": int(product["id"]),
                "name": str(product["title"]),
                "price": float(product["price"]),
                "imageUrl": product.get("thumbnail", "")
            })

        return {"recommendations": recommendations}

    except Exception as e:
        print("RECOMMEND ERROR:", e)
        return {"recommendations": []}


# ================= RUN =================
import uvicorn

if __name__ == "__main__":
    uvicorn.run("app:app", host="0.0.0.0", port=10000)