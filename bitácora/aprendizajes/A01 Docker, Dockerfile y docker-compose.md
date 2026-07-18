# A01 Docker, Dockerfile y docker-compose

## Contexto
Siempre había escuchado hablar de Docker como una herramienta mágica que tenía que usarse por la facilidad que proporcionan los *contenedores*, pero nunca le dí mayor atención. En el mi tercer curso (Plan 2023) cursé la materia llamada **Ingeniería de Sistemas Intensivos en Datos** donde tocamos temas sobre el Big Data y bases de datos NoSQL; conceptos que eran completamente nuevos para mi. En el tema de Bases de Datos NoSQL trabajamos con Redis y MongoDB y fue donde tuve el primer contacto con Docker. 

Tuve que instalarlo para poder descargar Redis y MongoDB como contenedores, y ahi aprendí lo básico. Docker es una plataforma de virtualización que a diferencia de las máquinas virtuales tradicionales, usa contenedores que virtualizan las aplicaciones y sus dependencias, compartiendo el núcleo (kernel) del sistema operativo host.

Entonces la ventaja que nos ofreció Docker fue: evitar descargar el instalador de cada herramienta y configurarlo en nativo lo que lo hace super portable y cómodo de usar; te daba la herramienta lista para usarse. Con esto en mente, encontré un caso de uso preciso para el proyecto.

## Qué necesitaba resolver
- Como Render no soporta nativamente a Java[(1)], empaquetar el backend en una imagen Docker para poder desplegarlo en Render mediante un **Dockerfile** era una necesidad. Render permite esta ruta[(2)]. Además, de esta forma aseguramos que lo que se ejecuta en producción sea idéntico a lo que se probó en local.

- Levantar una base de datos local para probar la aplicación durante el desarrollo. Usamos un contenedor de PostgreSQL que nos entrega la base de datos lista para usarse sin necesidad de configuración o instalación inicial; logramos esto a través de una **docker-compose**.


## Qué es
Un **Dockerfile** es un archivo que contiene una serie de instrucciones que le indican al motor de Docker *como* construir una **imagen**. 

Una **imagen** es el *template* que genera el Dockerfile. Contiene todas las instrucciones y archivos necesarios (código, bibliotecas, dependencias, variables de entorno) para ejecutar una aplicación.

Finalmente, cuando se ejecuta la imagen y se crea el **contenedor** que ejecuta el *template* de la aplicación. Efectivamente creando una versión autocontenida y ligera de, en este caso, el backend del proyecto.

Un **docker-compose** es un archivo donde se describe cómo se relacionan los contenedores. Es la forma declarativa y reutilizable de la forma habitual de ejecutar contenedores con `docker run`.

## Resultado
Siguiendo las guías[(5)],[(6)],[(7)] y ajustando a la versión de Java usada en el proyecto (17), se construyó un Dockerfile que fue capaz de reconstruir el esqueleto del backend. Esto significa que cuando esté listo, el Dockerfile será capaz de replicarlo en Render cuando se vaya a desplegar y nos da garantía de reproducibilidad.

En cuanto a la base de datos, en lugar de instalar la imagen de PostgreSQL directamente[(8)], se configuró un docker-compose donde además de instalar la imagen, se definió un **volumen** para almacenar los datos[(9)]. Un matiz importante es que los contenedores son efímeros por diseño, entonces `docker start/stop` solo pausan y reanudan el contenedor. Si llegáramos a necesitar una nueva versión del programa contenido o removemos el contenedor, perdemos los datos.

El volumen es un espacio de almacenamiento que Docker gestiona por separado del contenedor, entonces al definirlo en el compose, podemos eliminar o actualizar el contenedor y persistir los datos en el volumen. Al recrear o iniciar el contenedor, simplemente lee del `volume` determinado y es como si no hubiera pasado nada.


## Fuentes consultadas
- Render Supported Languages: 
https://render.com/docs/language-support
- How to deploy on Render from a Dockerfile: 
https://render.com/docs/docker
- Dockerfile overview: https://docs.docker.com/build/concepts/dockerfile/
- Docker Image vs. Container, las diferencias: https://www.ionos.es/digitalguide/servidores/configuracion/image-vs-container-docker/
- Guía Dockerfile para Spring boot:
https://docs.docker.com/get-started/docker-concepts/building-images/multi-stage-builds/
- Optimizing Docker Images for Spring Boot Apps with Multi-Stage Builds https://medium.com/@aymenfarhani28/optimizing-docker-images-for-spring-boot-apps-with-multi-stage-builds-c8ceeb2279cd
- How to Containerize Spring Boot Applications with Docker: https://oneuptime.com/blog/post/2026-02-20-java-spring-boot-docker/view
- Setup PostgreSQL on Windows with Docker: https://medium.com/@elanderson/setup-postgresql-on-windows-with-docker-15388766586f
- Frictionless Local Postgres with Docker Compose: https://github.com/asaikali/docker-compose-postgres



[(1)]: https://render.com/docs/language-support "Render Supported Languages"
[(2)]: https://render.com/docs/docker "Deploy on Render using Dockerfile"
[(5)]: https://docs.docker.com/get-started/docker-concepts/building-images/multi-stage-builds/ "Docker Multi-stage builds"
[(6)]: https://medium.com/@aymenfarhani28/optimizing-docker-images-for-spring-boot-apps-with-multi-stage-builds-c8ceeb2279cd "Docker Images for Spring Boot Apps"
[(7)]: https://oneuptime.com/blog/post/2026-02-20-java-spring-boot-docker/view "Containerize Spring Boot Apps"
[(8)]: https://medium.com/@elanderson/setup-postgresql-on-windows-with-docker-15388766586f "Setup PostgreSQL with Docker"
[(9)]: https://github.com/asaikali/docker-compose-postgres "Local PostgreSQL with Docker Compose"