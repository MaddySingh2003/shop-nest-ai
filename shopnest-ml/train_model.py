import pandas as pd
import requests
from sklearn.ensemble import RandomForestRegressor
from sklearn.preprocessing import LabelEncoder
import joblib
import numpy as np

url = "https://dummyjson.com/products?limit=200"
data = requests.get(url).json()["products"]

df = pd.DataFrame(data)

df["base_price"] = df["price"]
df["demand_score"]=np.random.uniform(0.3,1.0,len(df))
df["rating"] = np.random.uniform(3.0,5.0,len(df))

df["actual_price"]=(df["price"]*(1+df["rating"]/8)*(1+df["demand_score"]/2))

le_category = LabelEncoder()
le_brand = LabelEncoder()

df["category"] = le_category.fit_transform(df["category"].astype(str))
df["brand"] = le_brand.fit_transform(df["brand"].astype(str))

X = df[["category","brand","base_price","demand_score","rating"]]
y = df["actual_price"]

model = RandomForestRegressor(n_estimators=200)
model.fit(X,y)

joblib.dump(model,"model/price_model.pkl")
joblib.dump(le_category,"model/le_category.pkl")
joblib.dump(le_brand,"model/le_brand.pkl")
import pandas as pd
import requests
from sklearn.feature_extraction.text import TfidfVectorizer
import joblib

url = "https://dummyjson.com/products?limit=200"
data = requests.get(url).json()["products"]

df = pd.DataFrame(data)

# combine text fields
df["text"] = (
    df["title"].fillna("") + " " +
    df["description"].fillna("") + " " +
    df["brand"].fillna("") + " " +
    df["category"].fillna("")
)

vectorizer = TfidfVectorizer()

tfidf_matrix = vectorizer.fit_transform(df["text"])

joblib.dump(tfidf_matrix, "model/tfidf_matrix.pkl")
joblib.dump(vectorizer, "model/vectorizer.pkl")
joblib.dump(df, "model/products_df.pkl")

print("Recommendation model trained")