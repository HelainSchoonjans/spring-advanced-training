# Running the app

Deploy the docker compose stack.

Configure Vault.

# How to

# run docker compose

On Windows you need to add file-sharing on the repository folder first. Settings -> Resources -> File sharing

Then:

    docker-compose up

To find the docker name:

    docker ps

# create a shell to vault

Create an interactive bash process in a docker:

    docker exec -it workspace_vault_1 sh

# interact with the database

Create a shell to vault then:

    # connect to database
    psql --db formation-spring --user postgres
    # shows users
    \du

# run commands on vault

Authenticate

    vault login # use the credentials in docker-compose.yml

Add a key value in vault:

    vault kv put secret/poc-vault api_key=abcd api_secret=1234

Enable the database:

    vault secrets enable database

Configure database secrets:

    vault write database/config/formation_spring \
        plugin_name=postgresql-database-plugin \
        connection_url="postgresql://{{username}}:{{password}}@pg:5432/formation_spring?sslmode=disable" \
        username=postgres \
        password=postgres \
        allowed_roles="admin,member" 

Force password rotation:

    vault write -force database/rotate-root/formation_spring

Create identifiers:

    vault write database/roles/admin \
        db_name=formation_spring \
        creation_statements="CREATE ROLE \"{{name}}\"
        WITH LOGIN PASSWORD '{{password}}' VALID UNTIL '{{expiration}}'; \
        GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO \"{{name}}\";" \
        revocation_sql="SELECT revoke_access('{{name}}'); DROP user \"{{name}}\";" \
        default_ttl="1h" \
        max_ttl="24h"

    vault write database/roles/member \
        db_name=formation_spring \
        creation_statements="CREATE ROLE \"{{name}}\" \
        WITH LOGIN PASSWORD '{{password}}' VALID UNTIL '{{expiration}}'; \
        GRANT SELECT ON Book TO \"{{name}}\"; \
        GRANT SELECT, INSERT, UPDATE ON Member TO \"{{name}}\"; \
        GRANT SELECT, INSERT, UPDATE, DELETE ON Reservation TO \"{{name}}\";" \
        revocation_sql="SELECT revoke_access('{{name}}'); DROP user \"{{name}}\";" \
        default_ttl="10m" \
        max_ttl="20m"