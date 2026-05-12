package com.fitflow.clover.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fitflow.clover.core.theme.CloverGreen
import com.fitflow.clover.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermDetailScreen(navController: NavController, title: String) {
    Scaffold(
        containerColor = Color.White,
        topBar = { CenterAlignedTopAppBar(colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White), title = { Text("약관 동의") }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Image(painter = painterResource(id = R.drawable.back), contentDescription = null, modifier = Modifier.size(28.dp)) } }) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp)) {
            Text(title, fontWeight = FontWeight.Bold)
            Box(modifier = Modifier.fillMaxWidth().weight(1f).border(1.dp, Color.LightGray).padding(16.dp)) { Text("상세 내용...") }
            Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth().height(55.dp), colors = ButtonDefaults.buttonColors(containerColor = CloverGreen)) { Text("확인", color = Color.Black) }
        }
    }
}