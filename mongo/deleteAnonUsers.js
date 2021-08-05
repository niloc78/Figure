exports = async function() {

  // Get Atlas Parameters and application id
  const AtlasPrivateKey = context.values.get("AtlasPrivateKey");
  const AtlasPublicKey = context.values.get("AtlasPublicKey");
  const AtlasGroupId = context.values.get("AtlasGroupId");
  const appId = 'figure-mdlbd';
  
  
  // Authenticate to Realm API
  const respone_cloud_auth = await context.http.post({
    url : "https://realm.mongodb.com/api/admin/v3.0/auth/providers/mongodb-cloud/login",
    headers : { "Content-Type" : ["application/json"],
                 "Accept" : ["application/json"]},
    body : {"username": AtlasPublicKey, "apiKey": AtlasPrivateKey},
    encodeBodyAsJSON: true
                                                  
  });
    
   const cloud_auth_body = JSON.parse(respone_cloud_auth.body.text());
   
   // Get the internal appId
  const respone_realm_apps = await context.http.get({
    url : `https://realm.mongodb.com/api/admin/v3.0/groups/${AtlasGroupId}/apps`,
    headers : { "Content-Type" : ["application/json"],
                 "Accept" : ["application/json"],
                 "Authorization" : [`Bearer ${cloud_auth_body.access_token}`]
    }
                                                  
  });
     
   const realm_apps = JSON.parse(respone_realm_apps.body.text());
   
   
   var internalAppId = "";
   
   realm_apps.map(function(app){ 
     if (app.client_app_id == appId)
     {
       internalAppId = app._id;
     }
     });
   
   
   // Get all realm users 
    const respone_realm_users = await context.http.get({
    url : `https://realm.mongodb.com/api/admin/v3.0/groups/${AtlasGroupId}/apps/${internalAppId}/users`,
    headers : { "Content-Type" : ["application/json"],
                 "Accept" : ["application/json"],
                 "Authorization" : [`Bearer ${cloud_auth_body.access_token}`]
    }
                                                  
  });
    
    
   const realm_users = JSON.parse(respone_realm_users.body.text());
   
   
   // Filter only anon-users 
   var usersToDelete = [];
   
  realm_users.map(function(user){ 
     if (user.identities[0].provider_type == "anon-user")
     {
       usersToDelete.push(user._id);
     }
     });
    console.log(JSON.stringify(usersToDelete));
    
    
    // Delete the users on the list
     usersToDelete.map(function(id){ 
     const respone_realm_users_delete =  context.http.delete({
    url : `https://realm.mongodb.com/api/admin/v3.0/groups/${AtlasGroupId}/apps/${internalAppId}/users/${id}`,
    headers : { "Content-Type" : ["application/json"],
                 "Accept" : ["application/json"],
                 "Authorization" : [`Bearer ${cloud_auth_body.access_token}`]
    }
    
     });
     });
    
};
