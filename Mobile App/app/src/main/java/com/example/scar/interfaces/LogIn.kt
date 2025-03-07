@file:OptIn(ExperimentalMaterial3Api::class)

package com.whitebatcodes.myloginapplication.interfaces

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.scar.R
import com.example.scar.network.Api
import com.example.scar.ui.theme.Global
import com.example.scar.ui.theme.Screen
import com.example.scar.ui.theme.UserInfo
import com.example.scar.ui.theme.UserTokens
import com.example.scar.ui.theme.cardBg
import com.example.scar.ui.theme.mainBg
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//import com.whitebatcodes.myloginapplication.MainActivity
//import com.whitebatcodes.myloginapplication.ui.theme.MyLoginApplicationTheme

@Composable
fun LoginForm(navController: NavController) {

    Surface(modifier = Modifier.fillMaxSize(),
        color = mainBg // Set the desired background color here
    ) {
//        Image(painter = painterResource(id = R.drawable.background),
//            contentDescription = "bg",
//            contentScale = ContentScale.FillBounds,
//            modifier=Modifier.fillMaxWidth()
//               )

        var credentials by remember { mutableStateOf(Credentials()) }
        val context = LocalContext.current


        Spacer(modifier = Modifier.height(20.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp)
            ) {
                Image(painter = painterResource(id = R.drawable.scar_logo),
                    contentDescription = "bg",
//            contentScale = ContentScale.FillBounds,
                    modifier= Modifier
                        .fillMaxWidth()
                        .size(250.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))

                Card{
                    LoginField(
                        value = credentials.login,
                        onChange = { data -> credentials = credentials.copy(login = data) },
//                modifier = Modifier.fillMaxWidth()
                    )
                    PasswordField(
                        value = credentials.pwd,
                        onChange = { data -> credentials = credentials.copy(pwd = data) },
                        submit = {
                            if (!checkCredentials(credentials, context,navController)) credentials = Credentials()
                        },
//                modifier = Modifier.fillMaxWidth()
                    )
//                    Spacer(modifier = Modifier.height(10.dp))
//                    LabeledCheckbox(
//                        label = "Remember Me",
//                        onCheckChanged = {
//                            credentials = credentials.copy(remember = !credentials.remember)
//                        },
//                        isChecked = credentials.remember
//                    )



                }
                Column(
                    modifier = Modifier
//                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("don't have an account?", style = TextStyle(color = Color.White))
                    ClickableText(
                        text = AnnotatedString("Register") ,
                        onClick = {
                                  Log.d("register", "register")
                        },
                        style = TextStyle(color = Color.White, textDecoration = TextDecoration.Underline))
                }

                Spacer(modifier = Modifier.height(60.dp))
                Button(
                    onClick = {
//                        Api.setupPubnub()
                        val dataModel = UserInfo(credentials.login,credentials.pwd,credentials.login)
                        val call: Call<UserInfo?>? = Api.retrofitService.postData(dataModel)

                        var success = false;

                        call!!.enqueue(object : Callback<UserInfo?> {
                                override fun onResponse(
                                    call: Call<UserInfo?>?,
                                    response: Response<UserInfo?>
                                ) {
                                    if (response.isSuccessful) {
                                        Log.d("Success", response.toString())
                                        success = true;
                                        Global.userToken = Api.retrofitService.getToken(credentials.login).toString()
                                        navController.navigate(Screen.MainScreen.route)
                                    } else {
                                        Log.d("Other", response.toString())

//                                        navController.navigate(Screen.MainScreen.route)

                                    }
                                }
                                override fun onFailure(call: Call<UserInfo?>?, t: Throwable) {
                                    if(t.toString().contains("kotlinx.serialization.json.internal.JsonDecodingException")){
                                        Log.d("Other", t.toString())
                                        success = true;
//                                        Global.userToken = Api.retrofitService.getToken(credentials.login).toString()

                                        Api.retrofitService.getToken(credentials.login).enqueue(object : Callback<UserTokens> {
                                            override fun onResponse(call: Call<UserTokens>, response: Response<UserTokens>) {
                                                if (response.isSuccessful) {
                                                    // Successful response, extract and use the token
                                                    val userTokens: UserTokens? = response.body()
                                                    val token: String? = userTokens?.token
                                                    if (token != null) {
                                                        Global.userToken = token
                                                        Log.d("Token", token)
                                                    } else {
                                                        Log.d("Error", "Token is null")
                                                    }
                                                } else {
                                                    // Handle unsuccessful response
                                                    Log.d("Error", "Unsuccessful response: ${response.code()}")
                                                }
                                            }

                                            override fun onFailure(call: Call<UserTokens>, t: Throwable) {
                                                // Handle failure
                                                Log.d("Error", "Failed to get token: ${t.message}")
                                            }
                                        })


                                        navController.navigate(Screen.MainScreen.route)
                                    }
                                    Log.d("Fail", t.toString()) // Corrected this line to log the error message
                                }
                            })
                        if(success == false) {
                            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show()

                        }


                    },
                    enabled = credentials.isNotEmpty(),
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        cardBg.copy(alpha = 0.9f)
                    ),
                ) {
                    Text("Register")
                }
            }
        }

}

fun checkCredentials(creds: Credentials, context: Context, navController: NavController): Boolean {
    if (creds.isNotEmpty()) {
//        navController.navigate(Screen.MainScreen.route)
        val dataModel = UserInfo(creds.login,creds.pwd,creds.login)
        val call: Call<UserInfo?>? = Api.retrofitService.postData(dataModel)
        var success = false

        call!!.enqueue(object : Callback<UserInfo?> {
            override fun onResponse(
                call: Call<UserInfo?>?,
                response: Response<UserInfo?>
            ) {
                if (response.isSuccessful) {
                    Log.d("Success", response.toString())
                    success = true;
                    Global.userToken = Api.retrofitService.getToken(creds.login).toString()
                    navController.navigate(Screen.MainScreen.route)
                } else {
                    Log.d("Other", response.toString())
                    navController.navigate(Screen.MainScreen.route)
                }
            }
            override fun onFailure(call: Call<UserInfo?>?, t: Throwable) {
                if(t.toString().contains("kotlinx.serialization.json.internal.JsonDecodingException")){
                    Log.d("Other", t.toString())
                    success = true;

                    Api.retrofitService.getToken(creds.login).enqueue(object : Callback<UserTokens> {
                        override fun onResponse(call: Call<UserTokens>, response: Response<UserTokens>) {
                            if (response.isSuccessful) {
                                // Successful response, extract and use the token
                                val userTokens: UserTokens? = response.body()
                                val token: String? = userTokens?.token
                                if (token != null) {
                                    Global.userToken = token
                                    Log.d("Token", token)
                                } else {
                                    Log.d("Error", "Token is null")
                                }
                            } else {
                                // Handle unsuccessful response
                                Log.d("Error", "Unsuccessful response: ${response.code()}")
                            }
                        }

                        override fun onFailure(call: Call<UserTokens>, t: Throwable) {
                            // Handle failure
                            Log.d("Error", "Failed to get token: ${t.message}")
                        }
                    })
                    navController.navigate(Screen.MainScreen.route)
                }
                Log.d("Fail", t.toString()) // Corrected this line to log the error message
            }
        })
        if (success == false)
        {
            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show()

        }
        else if(success == true)
        {
            navController.navigate(Screen.MainScreen.route)
        }
//        context.startActivity(Intent(context, MainActivity::class.java))
//        (context as Activity).finish()
        return true
    } else {
        Toast.makeText(context, "Wrong Credentials", Toast.LENGTH_SHORT).show()
        return false
    }
}

data class Credentials(
    var login: String = "",
    var pwd: String = "",
    var remember: Boolean = false
) {
    fun isNotEmpty(): Boolean {
        return login.isNotEmpty() && pwd.isNotEmpty()
    }
}


@Composable
fun LabeledCheckbox(
    label: String,
    onCheckChanged: () -> Unit,
    isChecked: Boolean
) {

    Row(
        Modifier
            .clickable(
                onClick = onCheckChanged
            )
            .padding(4.dp)
    ) {
        Checkbox(checked = isChecked, onCheckedChange = null)
        Spacer(Modifier.size(6.dp))
        Text(label)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginField(
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Login",
    placeholder: String = "Enter your Login"
) {

    val focusManager = LocalFocusManager.current
    val leadingIcon = @Composable {
        Icon(
            Icons.Default.Person,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }

    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        leadingIcon = leadingIcon,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = VisualTransformation.None
    )
}

@Composable
fun PasswordField(
    value: String,
    onChange: (String) -> Unit,
    submit: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Password",
    placeholder: String = "Enter your Password"
) {

    var isPasswordVisible by remember { mutableStateOf(false) }

    val leadingIcon = @Composable {
        Icon(
            Icons.Default.Key,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }
    val trailingIcon = @Composable {
        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
            Icon(
                if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }


    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onDone = { submit() }
        ),
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}


