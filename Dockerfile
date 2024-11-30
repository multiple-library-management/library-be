# Use the official PostgreSQL base image
FROM postgres:latest

# Set environment variables for the PostgreSQL configuration
ENV POSTGRES_USER=root
ENV POSTGRES_PASSWORD=root
ENV PGDATA=/data/postgres

# Install pg_cron
RUN apt-get update && apt-get install -y \
  postgresql-$PG_MAJOR-cron \
  && apt-get clean \
  && rm -rf /var/lib/apt/lists/*

# Expose the PostgreSQL port
EXPOSE 5432

# Set the default command
CMD ["postgres"]