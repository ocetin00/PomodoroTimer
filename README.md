# 
The Pomodoro Timer makes it easy to manage time and tasks


<h1 align="center">Pomodoro Timer</h1>

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat"/></a>
</p>

<p align="center">  
This app has these features: task management, time management, and statistics graphs
  <br>
</p>
</br>

<p align="center">
<img src="https://github.com/ocetin00/temp/blob/main/pomodoro.gif" width="250"/>
</p>




# Download

<a href='' target='_blank'><img height='36' style='border:0px;height:36px;' src='https://developer.android.com/images/brand/en_app_rgb_wo_45.png' border='0'  /></a>

## Architecture overview

The app architecture has three layers: a [data layer](https://developer.android.com/jetpack/guide/data-layer), a [domain layer](https://developer.android.com/jetpack/guide/domain-layer) and a [UI layer](https://developer.android.com/jetpack/guide/ui-layer).


<center>
<img src="https://github.com/ocetin00/temp/blob/main/architecture-1-overall.png" width="600px" alt="Diagram showing overall app architecture" />
</center>


The architecture follows a reactive programming model with [unidirectional data flow](https://developer.android.com/jetpack/guide/ui-layer#udf). With the data layer at the bottom, the key concepts are:


*   Higher layers react to changes in lower layers.
*   Events flow down.
*   Data flows up.

The data flow is achieved using streams, implemented using [Kotlin Flows](https://developer.android.com/kotlin/flow).


## Tech stack & Open-source libraries
- Minimum SDK level 24
- Jetpack Compose
- Hilt 
- Navigation 
- Flow
- Splash Screen Api
- Data Store
- Toggle Tab  => com.github.ocetin00:ToggleTab
- Firebase-crashlytics
- Room
- Chart => com.githPhilJay:MPAndroidChartub
- Some accompanist libs => navigation-animation, systemuicontroller


# License
```xml
Developed by 2022 ocetin00 (Oğuzhan Çetin) and designed Burak Koçak 
 
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
