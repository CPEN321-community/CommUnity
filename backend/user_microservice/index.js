const express = require('express');
const bodyParser = require('body-parser');
const routes = require('./routes');
const cors = require('cors');
const db = require('./models');

const app = express();

app.use(express.json());
// app.use(cors({
//   origin: 'http://localhost:3000',
//   credentials: true
// }));
app.use(routes);
// app.use(express.urlencoded({ extended: true }));

db.sequelize.sync().then((req) => {
  // app.listen(3031, () => {
  //   console.log("MySQL server running on http://localhost:3031");
  // })
}).catch(e => console.log(e));

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}.`);
});
