
# run docker compose

On windows you need to add filesharing on the repository folder first. Settings -> Resources -> File sharing

Then:

    docker-compose up

# create a shell to vault

    docker exec -it workspace_vault_1 sh

# run commands on vault

Authenticate

    vault login # use the credentials in docker-compose.yml

Add a key value in vault:

    vault kv put secret/poc-vault api_key=abcd api_secret=1234

More tools in the slides of the training

