lines1 = new File(args[0]).readLines()
lines2 = new File(args[1]).readLines()
i = 0
for(; i < lines1.size(); i++){
  print lines1.get(i) + "\t"
  if(i < lines2.size()){
   print lines2.get(i)
  }
  println ""
}
for(; i < lines2.size(); i++){
  println "\t" + lines2.get(i)
}
