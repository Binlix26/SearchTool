# SearchTool

<p>
  This Java Application is to help you locate the '.txt' files that cotain the information you want, 
  it also give you the ability to get revelant words if you enable the database functionality 
  (local Mysql database to store synonyms).
</p>

<p>
Can be downloaded and run directly without any local dababase, just make sure choose 'disable database' in the UI
</p>

<h3>Core Techniques</h3>
<ul>
  <li>Inverted Index</li>
  <li>JavaFX</li>
  <li>JDBC</li>
  <li>MySQL</li>
</ul>

<h3>Structure of Inverted Index</h3>
<p>The inverted index will be printed in the console after it is created.</p>
<p>
do	3 -> [ 1, 2:[2, 10]], [ 3, 3:[6, 8, 10]], [ 4, 3:[1, 2, 3]]
</p>

<p>
do appears 3 times -> file id=1, appear twice(position 2 and 10) etc
</p>

<p>Simple text file added: <b>test search</b></p>
