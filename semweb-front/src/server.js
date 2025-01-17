const express = require('express');
const cors = require('cors');
const { exec } = require('child_process');
const app = express();
const port = 3000;

app.use(cors({
  origin: 'http://localhost:8080', 
  methods: 'GET,HEAD,PUT,PATCH,POST,DELETE',
  allowedHeaders: 'Content-Type,Authorization'
}));

app.get('/run-pokemonRDFGenerator', (req, res) => {
  exec('java -cp .\src\main\java\emweb\pokemonRDFGenerator.java emweb.pokemonRDFGenerator', (error, stdout, stderr) => {
    if (error) {
      console.error(`Error executing Java program: ${error}`);
      return res.status(500).send(`Error executing Java program: ${error}`);
    }
    console.log(`stdout: ${stdout}`);
    console.error(`stderr: ${stderr}`);
    res.send(`Java program executed successfully: ${stdout}`);
  });
});

app.listen(port, () => {
  console.log(`Server running at http://localhost:${port}`);
});