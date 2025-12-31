# Simple Clipboard

Uma biblioteca Java simples e leve para manipulação de **Clipboard (área de transferência)** usando **JavaFX**, com foco em **arquitetura limpa**, **testabilidade** e **facilidade de uso**.

---

## ✨ Objetivo

O objetivo do **simple-clipboard** é fornecer uma API mínima e confiável para:
- Copiar texto para o clipboard
- Ler texto do clipboard

Tudo isso sem expor detalhes internos do JavaFX para quem consome a biblioteca.

---

## 📦 Instalação (Maven Local)

Após publicar a lib localmente:

```bash
./gradlew publishToMavenLocal
```

Adicione ao seu projeto:

```gradle
repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("megalodonte:simple-clipboard:1.0.0")
}
```

---

## 🚀 Uso Básico

```java
import megalodonte.Clipboard;

Clipboard.setString("Olá mundo");

String texto = Clipboard.getString();
System.out.println(texto);
```

---

## 🧠 Arquitetura

A biblioteca segue o **Princípio da Inversão de Dependência (DIP)**:

```
Clipboard (API pública)
   ↓
ClipboardProvider (interface)
   ↓
FxClipboardProvider (implementação JavaFX)
```

Isso permite:
- Testes unitários sem JavaFX
- Uso de Mockito
- Evolução futura (ex: outra implementação de clipboard)

---

## 🧪 Testes

Os testes são **100% unitários**, usando **JUnit 5 + Mockito**, sem dependência de:
- JavaFX Thread
- Sistema operacional
- Ambiente gráfico

Exemplo de teste:

```java
ClipboardProvider provider = mock(ClipboardProvider.class);
Clipboard.setProvider(provider);

Clipboard.setString("teste");

verify(provider).setString("teste");
```

---

## 🔧 Tecnologias

- Java 21 (LTS)
- JavaFX 17
- JUnit 5
- Mockito
- Gradle

---

## ⚠️ Observações Importantes

- Java 25 **não é suportado** por Mockito/ByteBuddy no momento
- Recomendado usar **Java 21 LTS**
- A biblioteca é voltada para **texto**, não arquivos ou imagens

---

## 📁 Estrutura do Projeto

```
src/
 ├─ main/java/megalodonte/
 │   ├─ Clipboard.java
 │   ├─ ClipboardProvider.java
 │   └─ FxClipboardProvider.java
 │
 └─ test/java/megalodonte/
     └─ ClipboardTest.java
```

---

## 📜 Licença

MIT License

---

## 👨‍💻 Autor

Projeto desenvolvido por **Megalodonte**.# adb-file-pusher
