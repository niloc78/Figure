exports = function(authEvent) {
  if (authEvent.operationType !== "CREATE") { return }
  
  const myUser = authEvent.user;
  const myUserEmail = myUser.data.email;
  
  const collection = context.services.get("mongodb-atlas").db("Figure").collection("Users");
  collection.updateOne({email: myUserEmail}, {$set: {userid: myUser.id}}
  ).catch(err => console.error("failed to update"));
  return;
  
  /*const mongodb = context.services.get("mongodb-atlas");
  const user = authEvent.user;
  const useremail = user.get("data").get("email");
  const users = mongodb.db("Figure").collection("Users");
  */
  
  /*return users.updateOne(
      { email: useremail},
      { $set: { userid: user.id, hello: "hi"} }
    ).catch(console.error)
  */
  /*
    An Authentication Trigger will always call a function with an authEvent.
    Documentation on Triggers: https://docs.mongodb.com/realm/triggers/overview/

    Access the user associated with the authEvent:
    const user = authEvent.user

    Access the time the authEvent happened:
    const time = authEvent.time

    Access the operation type for the authEvent:
    const operationType = authEvent.operationType

    Access the providers associated with the authEvent:
    const providers = authEvent.providers

    Functions run by Triggers are run as System users and have full access to Services, Functions, and MongoDB Data.

    Access a mongodb service:
    const collection = context.services.get("<SERVICE_NAME>").db("<DB_NAME>").collection("<COLL_NAME>");
    const doc = collection.findOne({ name: "mongodb" });

    Call other named functions if they are defined in your application:
    const result = context.functions.execute("function_name", arg1, arg2);

    Access the default http client and execute a GET request:
    const response = context.http.get({ url: <URL> })

    Learn more about http client here: https://docs.mongodb.com/realm/functions/context/#context-http
  */
};