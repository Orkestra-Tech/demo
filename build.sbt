lazy val orkestra = orkestraProject("orkestra", file("orkestra"))
  .settings(
    libraryDependencies ++= Seq(
      "tech.orkestra" %%% "orkestra-github" % orkestraVersion,
      "tech.orkestra" %%% "orkestra-cron" % orkestraVersion
    )
  )
lazy val orkestraJVM = orkestra.jvm
lazy val orkestraJS = orkestra.js
