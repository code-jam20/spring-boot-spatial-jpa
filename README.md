# Geo Spatial Queries using MySQL Geometry

There are use cases when we need to find near by places or people in any modern
application development. This code shows a example of creating a simple POJO persistence. 
Spring Boot Spatial.

You can add **SPATIAL INDEX** to speed up search.

<br />

###### Example to search near by customers using spatial column:
```
localhost:8080/spatial/secure/customers/nearby?Latitude=37.856575&Longitude=-121.698989&Format=km
```

```
[
   {
      "distance":55.4,
      "distanceFormat":"km",
      "firstName":"John",
      "lastName":"Doe"
   },
   {
      "distance":49.6,
      "distanceFormat":"km",
      "firstName":"Brain",
      "lastName":"Lara"
   },
   {
      "distance":87.5,
      "distanceFormat":"km",
      "firstName":"Kyle",
      "lastName":"Doe"
   }
]
```