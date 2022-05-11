FROM postgres:13

ENV POSTGRES_PASSWORD=s3nh4fas123
ENV POSTGRES_DB=fas_sgi_v4

WORKDIR /usr/local/sgi

COPY database_test /usr/local/sgi

EXPOSE 5432