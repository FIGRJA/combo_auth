****This small mod allows players with Ely.by's auth system to enter the server with `online_mode=true` setting without interfering with Mojang's auth system.

The mod just combines two or more authentication systems without interfering with the server itself.

First, authentication occurs on the **Mojang** servers, and after an unsuccessful login, a check occurs on **Ely.by** or other servers, depending on the config. If it didn't pass either, then the player can't join the server.

In order to achieve this result, it was necessary to download the patch from the official site of Ely.by, which wasn't convenient and prevented players with official accounts from logging in.

When using this mod, it's recommended to change `enforce-secure-profile=true` to `enforce-secure-profile=false` in the `server.properties` file to disable message signing that **Ely.by** players don't have.

## At the moment, I have managed to create an agent with this idea for other server cores besides fabric in [combo-auth-agent](https://github.com/FIGRJA/combo-auth-agent).

Config example:
```json
"AuthList":[ "(order of execution)" ],
"AuthSchema":{
    "(name)":{
        "url_check": "(hasJoined request)"
        "url_property" : "(texture request)"
        "AddProperty" : [{"(custom)" : "(property)"},
            {"name":"ely","value":"but why are you asking?"}]
    }
},
"debug" : "all/detail/(nothing)"
```