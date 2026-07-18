# A02 Integración continua con GitHub Actions

## Contexto
El primer contacto que tuve con Github Actions fue en la materia de Mantenimiento y Pruebas del Software (Plan 2010). Cuando empezamos a ver el tema de Pruebas Unitarias, se dió una clase de "Pequeña intro a Git y
CI/CD con GitHub actions" en la que se definió git como algo más que "push y pull" y se hizo un ejercicio introductorio de montar un workflow CI/CD; filosofía clave en DevOps.

También se trató la opción de añadir una acción que ayudara a verificar la cobertura de código con JaCoCo[(7)] que se implementará en futuras Sprints.

## Qué necesitaba resolver
Una forma de asegurar que cada cambio que subo al repositorio se compile y pase los tests automáticamente,
para no hacer merge en main de código que rompa la aplicación. Con un workflow puedo detectar fallos antes de producción y tener un entorno controlado de desarrollo; por el momento es fase pura de CI (Continuous Integration). Más adelante, se extenderá el flujo propuesto para condicionar el despliegue a Render a que esa comprobación esté "en verde" (Continuous Deployment).

## Qué es
GitHub Actions ejecuta "workflows": ficheros YAML dentro de .github/workflows/ que
GitHub detecta y ejecuta automáticamente ante *triggers* como `push` o un `pull request`. Mi
workflow levanta una máquina virtual ubuntu, descarga el código, instala Java 17, y ejecuta `mvn verify`: una versión idéntica a la que aprendimos en clase que se encarga de descargar dependencias, compilar, corre los tests y
empaqueta el .jar.

Si cualquiera de esos pasos falla, el workflow sale en rojo y GitHub lo marca en el pull request, impidiendo el merge gracias a una **Branch protection rule**[(3)] que bloquea la funcionalidad de `merge` si la build actual no pasa el workflow.

Además un monta iun PostgreSQL de efímero[(4)]: vive solo mientras dura la
ejecución. Esta funcionalidad vendrá de utilidad con pruebas de integración futuras.

## Problemas encontrados
En el commit donde subí el esqueleto del proyecto ([Commit 73c9251](https://github.com/Fjjorda/casa-esperanza/commit/73c9251a84b48ee03a3e57b76cc05f7f2d92c1bd)), el workflow falló indicando:
```java
Error: Failed to execute goal org.apache.maven.plugins:maven-surefire-plugin:3.5.6:test (default-test) on project backend:
Error:
Error: See /home/runner/work/casa-esperanza/casa-esperanza/backend/target/surefire-reports for the individual test results.
Error: See dump files (if any exist) [date].dump, [date]-jvmRun[N].dump and [date].dumpstream.
Error: -> [Help 1]
Error:
Error: To see the full stack trace of the errors, re-run Maven with the -e switch.
Error: Re-run Maven using the -X switch to enable full debug logging.
Error:
Error: For more information about the errors and possible solutions, please read the following articles:
Error: [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoFailureException
Error: Process completed with exit code 1.
```

Que a primer vista no pude deducir cual era el origen del fallo. Curiosamente, antes del commit había ejecutado en local `mvn verify` y todo iba bien entonces recurrí a la IA para arreglarlo y no ensuciar el historial de commits pero no me dio una respuesta concluyente.

Me pedía que le pasara todo el código de error buscando lo que dijera "Caused by: " para identificar el origen pero no aparecía en el trace del error.

Finalmente me dió información que resultó util: 
```yml
- name: Guardar informe de tests si fallan
        if: failure()
        uses: actions/upload-artifact@v4
        with:
          name: surefire-reports
          path: backend/target/surefire-reports/
```
Incluir esto en el CI workflow me generaría el el stack trace completo tras ejecutar con `mvn -B verify -e` como un artefacto descargable; asi podría inspeccionar esos Caused by. Así fue como dí con la causa principal:

```yml
 ~[surefire-booter-3.5.6.jar:3.5.6] Caused by: org.postgresql.util.PSQLException: FATAL: password authentication failed for user "casa"
```

Resulta que había generado la conexión en el workflow con la base de datos con credenciales erróneas. En mi backend se conectaba con **BDcasa** y en el workflow con **casa**. Por eso me funcionaba en local y fallaba en el commit.

Tras unificar las credenciales todo marcó verde.

Investigando más a fondo sobre artifacts y como puedo mejorar el workflow se incluyó `if-no-files-found` para cuando los tests fallen antes de que Surefire genere el informe, continua y no falla la acción mostrando una compilación errónea como un falso positivo[(6)]. 


## Fuentes consultadas
- Building and testing Java with Maven: https://docs.github.com/en/actions/tutorials/build-and-test-code/java-with-maven
- Understanding GitHub Actions: https://docs.github.com/en/actions/get-started/understand-github-actions
- Managing a branch protection rule: https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/managing-protected-branches/managing-a-branch-protection-rule
- Communicating with Docker service containers (Postgres efímero): https://docs.github.com/en/actions/using-containerized-services/about-service-containers
- Workflow artifacts: https://docs.github.com/en/actions/concepts/workflows-and-actions/workflow-artifacts
- Documentación adicional de upload-artifact: https://github.com/actions/upload-artifact
- JaCoCo Code Coverage Reporter: https://github.com/marketplace/actions/jacoco-reporter

[(3)]: https://docs.github.com/en/repositories/ "Create Branch Protection Rule"
[(4)]: https://docs.github.com/en/actions/using-containerized-services/about-service-containers "Docker efímero en CI"
[(6)]: https://github.com/actions/upload-artifact "if-no-files-found Artifact"
[(7)]: https://github.com/marketplace/actions/jacoco-reporter "JaCoCo Code Reporter"