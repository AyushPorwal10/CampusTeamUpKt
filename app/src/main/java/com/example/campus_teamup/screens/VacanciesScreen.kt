package com.example.campus_teamup.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.campus_teamup.R
import com.example.campus_teamup.myThemes.PrimaryBlack
import com.example.campus_teamup.myThemes.PrimaryWhiteGradient
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.Black
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.LightWhite
import com.example.campus_teamup.ui.theme.White

@Preview
@Composable
fun VacanciesScreen() {
    val textColor = White
    val bgColor = BackGroundColor

    var queryText by remember { mutableStateOf("") }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {


        ConstraintLayout(modifier = Modifier.fillMaxSize()) {

            val (searchBar , filterIcon , divider, vacancyList) = createRefs()

            OutlinedTextField(value = queryText,
                onValueChange = { searchQuery -> queryText = searchQuery },
                colors = TextFieldStyle.myTextFieldColor(),
                shape = TextFieldStyle.defaultShape,
                modifier = Modifier.constrainAs(searchBar){
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)

                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search),
                        style = MaterialTheme.typography.titleMedium,
                        color = textColor
                    )
                },
                leadingIcon = {
                    Icon(

                        painter = painterResource(id = R.drawable.search),
                        contentDescription = null,
                        tint = White,
                        modifier = Modifier.size(22.dp)
                    )
                })

            Icon(
                painter = painterResource(id = R.drawable.filter),
                contentDescription = null,
                tint = White,
                modifier = Modifier
                    .size(26.dp)
                    .constrainAs(filterIcon) {
                        start.linkTo(searchBar.end, margin = 8.dp)
                        end.linkTo(parent.end)
                        top.linkTo(searchBar.top)
                        bottom.linkTo(searchBar.bottom)
                    }
            )

            // horizontal arrangement of search items
            createHorizontalChain(searchBar , filterIcon , chainStyle = ChainStyle.Spread)

            HorizontalDivider(thickness = 1.dp , color = BorderColor ,
                modifier = Modifier.constrainAs(divider){
                    start.linkTo(parent.start)
                    top.linkTo(searchBar.bottom , margin = 16.dp)
                    end.linkTo(parent.end)
                })


            // showing list of vacancies

            ShowListOfVacancies(
                modifier = Modifier.padding(5.dp).constrainAs(vacancyList) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(divider.bottom , margin = 5.dp)
                }
            )





        }

    }
}

@Composable
fun ShowListOfVacancies(modifier: Modifier) {

    LazyColumn(modifier = modifier , verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(10) {
            SingleVacancy(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }

}
