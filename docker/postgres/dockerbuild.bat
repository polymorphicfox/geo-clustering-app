call docker build -t polymorphicfox/geo-data-db .
call docker tag build -t polymorphicfox/geo-data-db build -t polymorphicfox/geo-data-db:latest
call docker push polymorphicfox/geo-data-db:latest