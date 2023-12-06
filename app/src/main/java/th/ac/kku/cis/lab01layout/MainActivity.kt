package th.ac.kku.cis.lab01layout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import th.ac.kku.cis.lab01layout.ui.theme.LAB01LayoutTheme

data class Person(val name:String,
                  val studentID:String,
                  val imageID:Int)

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LAB01LayoutTheme {
                // A surface container using the 'background' color from the theme
                PersonListApp()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun PersonListApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    var currentScreen = backStackEntry?.destination?.route ?: "PersonList"
    if(currentScreen.contains("/"))
        currentScreen = currentScreen.split("/")[0]
    var context = LocalContext.current
    var persons: List<Person> = listOf<Person>(
        Person("Mickey Mouse", "001", R.drawable.mickey_avatar),
        Person("Minnie Mouse", "002", R.drawable.mickey_avatar),
        Person("Donald Duck", "003", R.drawable.mickey_avatar),
        Person("Goofy", "004", R.drawable.mickey_avatar)
    )
    Scaffold (
        topBar = { TopAppBar(
            title = { Text("Main", color = Color.White)},
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = Color.Blue
            ),
            navigationIcon = {
                if(navController.previousBackStackEntry != null){
                    IconButton(onClick = {navController.navigateUp()}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            })
        }
    ) {
            it -> NavHost(
        navController = navController,
        startDestination = "PersonList",
        modifier = Modifier.padding(it)
    ){
        composable(route = "PersonList"){
            LazyColumn() {
                items(persons) {
                        data -> PersonListItem(data, onClick = {
                        id -> navController.navigate(route = "Person/"+id)
                })
                }
            }
        }
        composable(route = "Person/{id}"){
                it -> PersonDetail(navController = navController,
            userid = backStackEntry?.arguments?.getString("id"))
        }
    }
    }
}
@Composable
fun PersonDetail(
    navController: NavHostController = rememberNavController(),
    userid: String?
){
    var displayText = "Hello "
    if(userid != null)
        displayText += userid
    Text(text = displayText)
}
@Composable
fun PersonListItem(data:Person, onClick:(id: String) -> Unit){
    Row(modifier = Modifier
        .padding(vertical = 10.dp)
        .clickable { onClick(data.studentID) }) {
        Image(
            painter = painterResource(id = data.imageID),
            contentDescription = data.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
        )
        Column(modifier = Modifier.padding(4.dp),
            verticalArrangement = Arrangement.Center) {
            Text(data.name)//Changed
            Text(data.studentID)//Changed
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonListItemPreview(){
    PersonListApp()
}