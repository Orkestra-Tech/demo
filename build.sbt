lazy val orkestra = orkestraProject("orkestra", file("orkestra"))
  .settings(
    libraryDependencies ++= Seq(
      "com.goyeau" %%% "orkestra-github" % orkestraVersion,
      "com.goyeau" %%% "orkestra-cron" % orkestraVersion
    )
  )
lazy val orkestraJVM = orkestra.jvm
lazy val orkestraJS = orkestra.js
