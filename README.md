# Simple Web Crawler
Built using java11 and maven

### Build
```
git clone 
mvn clean package
```
### Test
```
mvn test
```

### Usage
```
cd target && java -jar crawler.jar https://cuvva.com
```

### Sample output
```json
{
    "baseUrl": "https://gnais.co",
    "broken": [],
    "externalUrls": [
        "https://medium.com/@AbuGnais",
        "https://stackoverflow.com/users/1222904/abugnais",
        "https://github.com/abugnais",
        "https://twitter.com/abugnais",
        "https://www.linkedin.com/in/ahmadmoqanasa/",
        "https://facebook.com/abugnais",
        "https://reddit.com/u/abugnais",
        "https://instagram.com/abugnais"
    ],
    "malformed": [],
    "pages": [
        {
            "children": [
                "https://medium.com/@AbuGnais",
                "https://stackoverflow.com/users/1222904/abugnais",
                "https://github.com/abugnais",
                "https://twitter.com/abugnais",
                "https://www.linkedin.com/in/ahmadmoqanasa/",
                "https://facebook.com/abugnais",
                "https://reddit.com/u/abugnais",
                "https://instagram.com/abugnais"
            ],
            "scripts": [
                "https://www.googletagmanager.com/gtag/js?id=UA-154766450-1"
            ],
            "styles": [
                "https://fonts.googleapis.com/css?family=Lato:300,400|Roboto+Slab:400,700|Roboto:300,300i,400,400i,500,500i,700,700i",
                "https://use.fontawesome.com/releases/v5.8.2/css/all.css",
                "https://gnais.co/style.css",
                "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
            ],
            "url": "https://gnais.co"
        }
    ]
}
```
