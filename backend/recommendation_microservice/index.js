const express = require('express');
const routes = require('./routes');
const Singleton = require('./singleton');
const model = (new Singleton()).getInstance();
const dotenv = require("dotenv");

dotenv.config({path: "../ports.env"});
dotenv.config();

const app = express();

app.use(express.json());
// app.use(cors({
//   origin: 'http://localhost:3000',
//   credentials: true
// }));
app.use(routes);

setInterval(() => {
  model.trainModel();
}, [36000 * 24]); // 24 hours

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}.`);
});