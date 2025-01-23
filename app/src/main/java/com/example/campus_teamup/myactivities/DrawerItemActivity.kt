package com.example.campus_teamup.myactivities


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.campus_teamup.R
import com.example.campus_teamup.screens.CodingProfiles
import com.example.campus_teamup.screens.CollegeDetails
import com.example.campus_teamup.screens.CollegeDetailsRedesign
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.Black
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.White


class DrawerItemActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val screenToOpen = intent.getStringExtra("DrawerItem")
            Profiles(screenToOpen!!)
        }
    }
}

@Composable

fun Profiles(screenToOpen: String = "") {
    val bgColor = BackGroundColor
    val textColor = White

    var isClicked by remember { mutableStateOf(false) }

    var selectedLayout by remember {
        mutableStateOf("collegeDetails")
    }

    ConstraintLayout(modifier = Modifier
        .background(BackGroundColor)
        .fillMaxWidth()
        .fillMaxHeight()) {

        val (topAppBar ,divider, codingProfileBtn , collegeDetailsBtn , areaToLoadProfiles) = createRefs()



        // TOP APP BAR
        Top_App_Bar(modifier = Modifier.constrainAs(topAppBar){})

        HorizontalDivider(modifier = Modifier
            .fillMaxWidth()
            .background(BorderColor)
            .constrainAs(divider) {
                top.linkTo(topAppBar.bottom)
            })

        // CODING PROFILE BUTTON and COLLEGE DETAILS BUTTON

        CollegeDetailsBtn(
            modifier = Modifier
                .constrainAs(collegeDetailsBtn) {
                    top.linkTo(topAppBar.bottom, margin = 20.dp)
                }
                .padding(all = 0.dp)
                .height(34.dp), selectedLayout , onClick = {
                isClicked = !isClicked
                selectedLayout = "collegeDetails"
            }
        )

        CodingProfilesBtn(
            modifier = Modifier
                .constrainAs(codingProfileBtn) {
                    top.linkTo(topAppBar.bottom, margin = 20.dp)
                }
                .padding(all = 0.dp)
                .height(34.dp), selectedLayout , onClick = {
                isClicked = !isClicked
                selectedLayout = "codingProfiles"
            }
        )


        createHorizontalChain(collegeDetailsBtn , codingProfileBtn , chainStyle = ChainStyle.Spread)



        val verticalScroll = rememberScrollState()

        Box(modifier = Modifier
            .verticalScroll(verticalScroll)
            .padding(20.dp)
            .fillMaxSize()
            .constrainAs(areaToLoadProfiles) {
                top.linkTo(collegeDetailsBtn.bottom, margin = 20.dp)
            } , contentAlignment = Alignment.Center){

            if(selectedLayout == "collegeDetails")
                CollegeDetails()
            else
                CodingProfiles()
        }

    }

}

@Composable
fun CodingProfilesBtn(modifier: Modifier, selectedLayout: String, onClick: () -> Unit) {

    OutlinedButton(onClick =onClick ,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selectedLayout == "codingProfiles") White else BackGroundColor
        )
    ) {
        Text(
            text = stringResource(id = R.string.codingProfileBtn),
            color = if(selectedLayout == "codingProfiles" ) Black else White
        )
    }
}

@Composable
fun CollegeDetailsBtn(modifier: Modifier , selectedLayout : String , onClick : () -> Unit) {
    OutlinedButton(onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selectedLayout == "collegeDetails") White else BackGroundColor
        )

    ) {
        Text(
            text = stringResource(id = R.string.collgeDetailsBtn),
            color = if(selectedLayout == "collegeDetails" ) Black else White
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Top_App_Bar(modifier: Modifier) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                color = White
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BackGroundColor,
            navigationIconContentColor = White
        ),
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(
                    painter = painterResource(id = R.drawable.browseback),
                    contentDescription = null,
                    tint = White
                )
            }
        }, modifier = modifier
    )
}
