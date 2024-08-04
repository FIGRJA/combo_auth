This small mod allows players with Ely.by accounts to enter the server with `online_mode=true` without interfering with players with Mojang accounts.

The mod just combines two or more authentication systems without interfering with the server itself.

First, authentication occurs on the **Mojang** servers, and after an unsuccessful login, a check occurs on **Ely.by** or other servers, depending on the config . If there and there did not pass the test, then the player is **not allowed** on the server.

In order to achieve this result, it was necessary to download the patch from the official site Ely.by, which was not convenient and prevented players with official accounts from logging in.

When using this mod, I recommend setting `enforce-secure-profile=false` in the server configuration to disable message signing that **Ely,by** players do not have.

## At the moment, I have managed to create an agent with this idea for other kernels besides fabric in [combo-auth-agent](https://github.com/FIGRJA/combo-auth-agent).

Config:
``` json
"AuthList":[ "order of execution" ],
"AuthSchema":{
    "name":{
        "url_check": "url request hasJoinedServer"
        "url_property" : "url reqest profile property (skin&cape)"
        "AddProperty" : [{"custom" : "property"},
            {"name":"ely","value":"why not"}]
    }
},
"dubug" : "all / detal / (not necessary)"     
```   
