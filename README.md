# TRABAJO FINAL NTEGRACIÓN CONTINUA EN EL DESARROLLO ÁGIL
# Moviecards - Sergio Plaza Gonzalo

A lo largo de este documento voy a explicar los cambios que he realizado en el código con el objetivo de completar el trabajo finap, partiendo de la base de la práctica 5 de la asignatura.

La url donde se puede ver el entorno de producción es [https://moviecards-plaza.azurewebsites.net/](https://moviecards-plaza.azurewebsites.net/)

La url donde se puede ver el entorno de Pre-rpoducción es [https://moviecards-pre-prod-plaza.azurewebsites.net/](https://moviecards-pre-prod-plaza.azurewebsites.net/)

## Uso del microservicio llamado moviecards-service
Para que esta aplicación hiciera uso del nuevo microservicio realicé los siguientes cambios:

  1. Modifiqué las clases `MovieServiceImpl.java` y `ActorServiceImpl.java` para que ya no hicieran uso de los componenetes JPA y en su luegar usaran RestTemplate para hacer peticiones al nuevo microservicio desplegado en Azure. Además ya que la respuesta que proporcionan los métodos de RestTemplate es distinta de la que proporcionaban los métodos de los componentes JPA también adapte los métodos de dichas clases para que el comportamiento de la aplicación no se viera afectado. Para mas detalles por favor reviar los cambios introducidos en [este commit](https://github.com/Kekon130/moviecards/commit/62be3a1975fbe0580217034d16e1af8b4313f2fd).

  2. Para ajustar los tests al uso del nuevo microservicio en esta ocasión solo tuve que modificar los test unitarios. Los cambios que hice fueron:

      * Para el test `shouldGetAll(Ators/Movies)` cambié la lista por un array de 2 posiciones y adapté la petición para que usase RestTemplate.

      * Para el test `shouldGet(Actor/Movie)ById` solo adapté la petición para que usase RestTemplate.

      * Eliminé los test `shouldSave(Actor/Movie)` ya que el nuevo microservicio no devuelve ningún objeto cuando se realiza una petición para persistir información y por tanto no había form de comprobar su funcionamiento.

      Para una información mas detallada revisar [este commit](https://github.com/Kekon130/moviecards/commit/580f13198e6bfa31e803063de29b73180df0b2c0)

## Entorno de Pre-Producción
Para crear el entorno de pre-producción primero cree un nuevo app-service en Azure y lo configuré igual que en la práctica 5 para que funcionase con mi repositorio de GitHub, se puede ver en [esta url](https://moviecards-pre-prod-plaza.azurewebsites.net/).

La lógica que seguí cuando incluí este nuevo entorno en mi automatización de GitHub Actions fue que en el entorno de producción se debían desplegar los cambios de la rama principal `main` y el resto de las ramas de debían de desplegar en pre-producción. Dicho esto este fue el trabajo que añadí:

  ``` yaml
  stage:
  runs-on: ubuntu-latest
  needs: qa
  if: github.ref != 'refs/heads/main'
  steps:
    - name: Download artifact from build job
      uses: actions/download-artifact@v4
      with:
        name: moviecards-java
    
    - name: Deploy to Azure Web App
      id: deploy-to-webapp
      uses: azure/webapps-deploy@v3
      with:
        app-name: 'moviecards-pre-prod-plaza'
        slot-name: 'Production'
        package: '*.jar'
        publish-profile: ${{ secrets.AZUREAPPSERVICE_PUBLISHPROFILE_66D776997F49437DA4DCFDAAE5188CC6 }}
  ```
Para más información revisar [este commit](https://github.com/Kekon130/moviecards/commit/ef265854c2324404c0b97a7bce39f00c18d4c518)

## Fecha de muerte de los Actores
Para incluir la fecha de fallecimiento de los actores realicé las siguientes modificaciones:

  1. Añadí el nuevo atributo `deathDate` a la clase Actor, así como sus respectivos getters y setters.

  2. Edité el formulario HTML de los actores para incluir un nuevo input para la fecha de fallecimiento.

  3. Modifiqué la vista HTML para el listado de actores y añadí una nueva columna con la fecha de fallecimiento.

  4. Modifiqué el End to End test de Actores:

      * En el test `testPageLoad` añadí una nueva linea para que buscara el input `deathDate` en el formulario.

      * en el test `testListActors` añadí una nueva línea para que confirmase si la 3 columna de la tabla era la `Fecha de Fallecimiento` y ajusté el resto de las columnas.

  5. Modifiqué el test unitario de los Actores para probar la fecha de fallecimiento.

Para mas información revisar [este commit](https://github.com/Kekon130/moviecards/commit/94fa65c8945dc9470b366babdad48e7caaaf0b48)

## Garantía de calidad.
Para asegurar la calidad de la aplicación modifiqué las reglas de sonnar-qube para que fallase con 5 errores críticos o más.

La aplicación antes de esto tenía 8 errores críticos por la tanto tuve que realizar algunos cambios para segurar que podía desplegarla.

La mayoría de errores críticos venían de que la aplicación estab usando muchos strings declarados directamente en el código. Esto hacía que se repitieran los mismos mensajes en más de un método y que, en caso de tener que realizar cambios, estos tuvieran que realizarse en varias líenas en lugar de solo 1.

Por esto cree una clase llamada `Dict.java` donde almaceno todos los mensajes y luego desde los controladores solo llamo a esos atributos. Para ver mejor estos cambios revisar [este commit](https://github.com/Kekon130/moviecards/commit/06baf68edf76905b1766b2615cb6b45d59eb8c5d#diff-eec050468856e5cbcce3422df08114830a4e51e437423aea755f16209565eec6).

Por último, no quise restringir todos los entornos con esta regla así que cree un nuevo trabajo en mi GitHub Actions llamado `qa-prod`. De esta forma los cambios se pueden desplegar en el entorno de Pre-Producción aunque sonnar fallé pero no en producción. Para ver más información sobre esto revisar [este commit](https://github.com/Kekon130/moviecards/commit/e1943f4216986e0798aa8f3316a610efe02be659)

## Consideraciones adicionales
Después de realizar las modificaciones para que esta aplicación usara el microservicio desplegado en Azure observé que las clases JPA ya no se estabn usando así que decidí borrarlas ya que no tenía sentido que siguieran estando. Las acciones de guardado en la base de datos ahora las hace `moviecards-service`.

Después de realizar este cambio observé que las pruebas de integración comenzaron a fallar ya que usaban las clases JPA, por tanto, observando que `moviecards-service` tambi´n implementaba dichas clases y que los métodos para persistir datos no devuelven ningún objeto decidí borrar también de este proyecto las pruebas de integración y mantenerlas solamente en el otro.