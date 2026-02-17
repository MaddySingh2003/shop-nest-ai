import pandas as pd
import numpy as np
import random
from sklearn.ensemble import RandomForestRegressor
from sklearn.preprocessing import LabelEncoder
import joblib
import os

# Create synthetic dataset
categories = ["Electronics", "Clothing", "Shoes", "Books", "Home"]
brands = ["Nike", "Samsung", "Apple", "Puma", "Sony", "Zara"]

data = []

for _ in range(1000):
    category = random.choice(categories)
    brand = random.choice(brands)
    base_price = random.randint(500, 50000)
    demand_score = round(random.uniform(0.3, 1.0), 2)
    rating = round(random.uniform(3.0, 5.0), 1)

    # Artificial pricing logic
    actual_price = base_price * (1 + demand_score * 0.2) + rating * 100

    data.append([
        category, brand, base_price, demand_score, rating, actual_price
    ])

df = pd.DataFrame(data, columns=[
    "category", "brand", "base_price",
    "demand_score", "rating", "actual_price"
])

os.makedirs("data", exist_ok=True)
df.to_csv("data/products.csv", index=False)


# Encode categorical
le_category = LabelEncoder()
le_brand = LabelEncoder()

df["category"] = le_category.fit_transform(df["category"].astype(str))
df["brand"] = le_brand.fit_transform(df["brand"].astype(str))


X = df[["category", "brand", "base_price", "demand_score", "rating"]]
y = df["actual_price"]

model = RandomForestRegressor(n_estimators=100)
model.fit(X, y)

os.makedirs("model", exist_ok=True)

joblib.dump(model, "model/price_model.pkl")
joblib.dump(le_category, "model/le_category.pkl")
joblib.dump(le_brand, "model/le_brand.pkl")

print("Model trained and saved successfully!")
