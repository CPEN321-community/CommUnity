const express = require('express');
const bodyParser = require('body-parser');
const routes = require('./routes');
const cors = require('cors');
const Singleton = require('./singleton');
const model = (new Singleton()).getInstance();

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

const PORT = process.env.PORT || 6969;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}.`);
});
