package edu.brown.cs.student.main.server.Data;

public class BUSRoute {
  private int agencyId;
  private double[] bounds;
  private String color;
  private String description;
  private int id;
  private boolean isActive;
  private String longName;
  private String shortName;
  private String textColor;
  private String type;
  private String url;

  public BUSRoute(
      int agencyId,
      double[] bounds,
      String color,
      String description,
      int id,
      boolean isActive,
      String longName,
      String shortName,
      String textColor,
      String type,
      String url) {
    this.agencyId = agencyId;
    this.bounds = bounds;
    this.color = color;
    this.description = description;
    this.id = id;
    this.isActive = isActive;
    this.longName = longName;
    this.shortName = shortName;
    this.textColor = textColor;
    this.type = type;
    this.url = url;
  }

  public int getAgencyId() {
    return agencyId;
  }

  public double[] getBounds() {
    return bounds;
  }

  public String getColor() {
    return color;
  }

  public String getDescription() {
    return description;
  }

  public int getId() {
    return id;
  }

  public boolean isActive() {
    return isActive;
  }

  public String getLongName() {
    return longName;
  }

  public String getShortName() {
    return shortName;
  }

  public String getTextColor() {
    return textColor;
  }

  public String getType() {
    return type;
  }

  public String getUrl() {
    return url;
  }
}
