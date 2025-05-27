db = db.getSiblingDB('image_requests_db');
db.createCollection('image_requests');
db.image_requests.createIndex({ description: "text" });
