from fastapi import FastAPI
import joblib
import numpy as np


app =FastAPI()

model = joblib.load("model/price_model.pkl")
le_category = joblib.load("model/le_category.pkl")
le_brand = joblib.load("model/le_brand.pkl")

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
            "predictes-price":round(float(prediction),2)
        }
    
    except Exception as e:
        return{"error":str(e)}
