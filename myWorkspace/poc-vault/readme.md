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

    psql --db formation-spring --user postgres
    \du

# run commands on vault

Authenticate

    vault login # use the credentials in docker-compose.yml

Add a key value in vault:

    vault kv put secret/poc-vault api_key=abcd api_secret=1234

More tools in the slides of the training

