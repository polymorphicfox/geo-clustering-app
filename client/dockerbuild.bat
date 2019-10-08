call docker build -t polymorphicfox/clustering-app-frontend .
call docker tag build -t polymorphicfox/clustering-app-frontend build -t polymorphicfox/clustering-app-frontend:latest
call docker push polymorphicfox/clustering-app-frontend:latest