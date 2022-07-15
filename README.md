# SecretsManager-demo

  This is a very crude example of using aws cli & instance profiles to access secrets manager. The purpose being to show people how to reduce the reliance on username and passwords stored in applications to communicate.
  
## Developing locally

When running on your local maching this code uses AWS Access Keys to int3erace with Secrets Manager. You will need to ensure the AWS role that is assigned to your account have the below permissions:

```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "VisualEditor0",
            "Effect": "Allow",
            "Action": [
                "secretsmanager:GetSecretValue",
                "secretsmanager:DescribeSecret",
                "secretsmanager:ListSecrets"
            ],
            "Resource": "*"
        }
    ]
}
```

You will also have to include your credentials in ~/.aws/credentials

```bash
mdigiacomi@mdigiacomi-desktop:~$ cat .aws/credentials 
[default]
aws_access_key_id=<value here>
aws_secret_access_key=<value here>
aws_session_token=<value here>
```

From here it is a simple:

```bash
$ ./mavenw clean package
$ java -jar ./target/rest-service-complete-0.0.1-SNAPSHOT.jar --server.port=8083
```

## Running on EC2

Create an instance profile in IAM that gives the instance similar permissions to the Developer workflow. Create a instance and assign the profile the instance. From there clone the repo and run the following commands:

```bash
$ ./mavenw clean package
$ java -jar ./target/rest-service-complete-0.0.1-SNAPSHOT.jar --server.port=8083
```

This will use the instance profile to access Secrets Manager to get the password. Saving you from storing it in the repo.
