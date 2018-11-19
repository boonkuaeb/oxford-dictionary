## Sample Dictionary Application

This application is a demonstration of the integrate LINE Bot and Oxford Dictionary API.

   ![QRCode](docs/qr-code.png "qr code chat")

### Prerequisite

1. LINE Message API account from [https://developers.line.me/en/](https://developers.line.me/en/).The parameters that we need from this step as below.
     * `channel-token` . A channel token value of LINE bot account.
     * `channel-secret`. A channel secret value of LINE bot account.
2. Oxford Dictionary Account from [https://developer.oxforddictionaries.com/sign-in](https://developer.oxforddictionaries.com/sign-in). The parameters that we need are.
    * `oxford_base_url`. An endpoint URL to connect to the Oxford Dictionary API.
    * `app_id`. A application id of Oxford Dictionary account.
    * `app_key` A application Key of Oxford Dictionary account.
    * `api_base_url`  API requests endpoint of Oxford Dictionary account.
    
3. To integration test with LINE Bot is required `HTTPS` So, we use **Narok** is handling `HTTPS` on local development environment
    * Register Ngrok Account : [https://dashboard.ngrok.com/user/signup](https://dashboard.ngrok.com/user/signup)
    * Download Ngrok application: [https://ngrok.com/download](https://ngrok.com/download)
    * Ngrok Manual: [https://ngrok.com/docs](https://ngrok.com/docs)
### Sep up Development environment
A step by step series of examples that tell you how to get a development env running
##### 1. Down load a source code
```git
git clone git@github.com:boonkuaeb/oxford-dictionary.git dictionary
```
##### 2. Edit application configuration files
Open the `src/main/resources/application.yml` with your favorite editor.
Replace contain of `application.yml` file as a table below.

| Parameter Name        | Definition|
| ------------- |:-------------:|
| ${DEVTOOLS_IS_DEV_MODE}      |  `true` for development env, `false` for production environment |
| ${LINE_BOT_CHANNEL_TOKEN}      | Replace this value as a `chanel-token` from the Prerequisite  step |
| ${LINE_BOT_CHANNEL_SECRET} | Replace this value as a `chanel-secret` from the Prerequisite  step     |
| ${OXFORD_BASE_URL} | Replace this value as a `api_base_url` from the Prerequisite  step     |
| ${OXFORD_APP_ID} | Replace this value as a `app_id` from the Prerequisite  step      |
| ${OXFORD_APP_KEY} | Replace this value as a `app_key` from the Prerequisite  step       |

##### 3. Unit Test
```bash
mvn clean test
```

##### 4. End to End Test
1. Prepare `HTTPS`
    * Run `./ngrok http 8080`
    * Find the `https` url that Ngrok generated. For example `https://random-url.ngrok.io`
2. Update LINE Bot a Webhook URL
    * Go to [https://developers.line.biz/console/](https://developers.line.biz/console/)
    * Select the provider and chanel that registered form Prerequisite step
    * Select Channel Tab.
    * Update the Webhook URL with pattern `https url` + `/callback`. In this case, I use `https://random-url.ngrok.io/callback`
   
3. Add The LINE channel to your LINE mobile application 
    * Go to [https://developers.line.biz/console/](https://developers.line.biz/console/)
    * Select the chanel that registered from the Prerequisite step
    * Select Channel Tab.
    * Find the QR code image
    * Open your LINE mobile application. 
    * Add new friend via scan QR code.
    * Check a Friends list on LINE application. You will see new friend name `Dictionary BOT`
    
4. Test Chanel
    * Chat to `Dictionary BOT`
    * Input message text : `LINE`
    * Result: 
    
      ![Chat](docs/test-chat.png "Test chat" )


### Create Docker Images.

1. Make sure all Unit Test passed properly. 
    ```$docker
    mvn clean test
    ```
2. Create docker image
    ```$docker
    mvn package -DskipTests=true docker:build -Ddictionary-profile=default
    ```
    Check `dictionary-default-img` created.
      ```docker
      docker images
      ```
      Find docker image name as  `dictionary-default-img`
      
3. Run container from docker image
    ```docker
     docker run --name dictionary-default -p8080:8080 -e DEVTOOLS_IS_DEV_MODE=false -e LINE_BOT_CHANNEL_TOKEN=your_line_bot_channel_token -e LINE_BOT_CHANNEL_SECRET=your_line_bot_channel_secret -e LINE_BOT_CHANNEL_TOKEN=your_line_bot_channel_token -e OXFORD_BASE_URL=your_oxford_base_url -e OXFORD_APP_ID=your_oxford_app_id -e OXFORD_APP_KEY=your_oxford_app_key -d dictionary-img-default
    ```
    _The values of the parameters are decent on your environment configuration._
4. Test the docker container is running

        Open URL [http://localhost:8080](http://localhost:8080) and check the page is showing `Whitelabel Error Page` message. That means we can run the application from docker container properly.


5. Store docker images to the Docker repository.
    I use [https://hub.docker.com/](https://hub.docker.com/). 
    * Open the terminal and login to docker-hub account as this step.
        ```docker
        docker login
        ```
        Input user/password of docker-hub account.
    * Check `dictionary-default-img` created.
        ```docker
        docker images
        ```
      Find the Image Id of `dictionary-default-img` image.
      For example I use `dictionary-default-image-id-xxx` as a Image Id of `dictionary-default-img`
        
    * Tags  the  `dictionary-default-image-id-xxx` Image Id from previous step.
        * `boonkuaeb/dictionary:latest` is Docker Hub repository name and version

        ```docker
          docker tag dictionary-default-image-id-xxx boonkuaeb/dictionary:latest
        ```
        
    * Push `boonkuaeb/dictionary:latest` image to Docker-Hub repository
      ```docker
      docker push boonkuaeb/dictionary
      ```
      Open [https://hub.docker.com/](https://hub.docker.com/) and find the boonkuaeb/dictionary that just pushed.
      
### Deploy the Docker Images.      
In this case, I plan to test deployment dictionary application on my local environment. So, I will clean up all container and images as a few dockers command below.

```docker
docker stop dictionary-default
```

```docker
docker rm dictionary-default
```


```docker
docker rmi dictionary-default-img
```

**Deployment step**
1. Pull and deploy docker image :

    The deployment command is very similar to step 3 of `Create Docker Images` step. The different things are. 
    * `-d ` is a image name : change to Docker Hub repository  name as a `boonkuaeb/dictionary`
    * `--name` is a container name: change to `dictionary-prod`
    * `DEVTOOLS_IS_DEV_MODE=false` 
    
    The command to deploy as a below.
    ```docker
    docker run --name dictionary-prod -p8080:8080 -e DEVTOOLS_IS_DEV_MODE=false -e LINE_BOT_CHANNEL_TOKEN=your_line_bot_channel_token -e LINE_BOT_CHANNEL_SECRET=your_line_bot_channel_secret -e LINE_BOT_CHANNEL_TOKEN=your_line_bot_channel_token -e OXFORD_BASE_URL=your_oxford_base_url -e OXFORD_APP_ID=your_oxford_app_id -e OXFORD_APP_KEY=your_oxford_app_key -d boonkuaeb/dictionary
    ```
2. Check the container is running :
    ```docker
    docker ps
    ```
    Find name container as `dictionary-prod` is running.
    
3. Check application is work:

    Open hosts file as a command `sudo vim /etc/hosts` add new line `127.0.0.1 dictionary-prod` and save the file.
    
    Open URL `http://dictionary-prod:8080` and check the page is showing Whitelabel Error Page message. That means we can run the application on docker container properly.
    

