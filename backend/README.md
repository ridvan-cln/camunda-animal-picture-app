# Getting Started
## Setting up the Environment Variables

Before you start the application, you need to set up the environment variables.
These contain the connection info to your Camunda SaaS Cluster.
This can be done by creating a `.env` file in the root directory of the project.

Here's an example of what your `.env` file should look like:
```env
ZEEBE_CLIENT_CLOUD_REGION=your_region
ZEEBE_CLIENT_CLOUD_CLIENT_ID=your_client_id
ZEEBE_CLIENT_CLOUD_CLIENT_SECRET=your_client_secret
ZEEBE_CLIENT_CLOUD_CLUSTER_ID=your_cluster_id
```
## Running the application
```
docker compose up -d
```
This starts the Spring Boot client and a MongoDB instance to store the pictures.

## Usage
### REST API
Starting a new process (returns processInstanceKey):
```
POST localhost:8080/animal-picture-process
```
After completing the User Task, the Job Worker will have the done the work as well.

To retrieve the picture:
```
GET localhost:8080/animal-picture-process/{processInstanceKey}
```
