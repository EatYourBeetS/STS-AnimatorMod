package eatyourbeets.events.replacement;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardTags;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.BloodVial;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import eatyourbeets.cards.animator.special.Special_Bite;
import eatyourbeets.cards.animator.special.Special_VampireBlood;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;
import java.util.List;

public class AnimatorVampires extends AbstractImageEvent
{
    public static final String ID = GR.Animator.CreateID(Vampires.ID);
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;
    private static final String ACCEPT_BODY;
    private static final String EXIT_BODY;
    private static final String GIVE_VIAL;
    private static final float HP_DRAIN = 0.3F;
    private int maxHpLoss;
    private int screenNum = 0;
    private boolean hasVial;
    private List<String> bites;

    public AnimatorVampires()
    {
        super(NAME, "test", "images/events/vampires.jpg");

        this.body = AbstractDungeon.player.getVampireText();
        this.maxHpLoss = MathUtils.ceil((float) AbstractDungeon.player.maxHealth * 0.3F);
        if (this.maxHpLoss >= AbstractDungeon.player.maxHealth)
        {
            this.maxHpLoss = AbstractDungeon.player.maxHealth - 1;
        }

        this.bites = new ArrayList<>();
        this.hasVial = AbstractDungeon.player.hasRelic("Blood Vial");
        this.imageEventText.setDialogOption(JUtils.Format(OPTIONS[0], this.maxHpLoss), new Special_Bite());

        String vialName = (new BloodVial()).name;
        String cardName = Special_VampireBlood.DATA.Strings.NAME;
        this.imageEventText.setDialogOption(JUtils.Format(OPTIONS[2], vialName, cardName), !this.hasVial, new Special_VampireBlood());

        this.imageEventText.setDialogOption(OPTIONS[1]);
    }

    protected void buttonEffect(int buttonPressed)
    {
        switch (this.screenNum)
        {
            case 0:
                switch (buttonPressed)
                {
                    case 0:
                        CardCrawlGame.sound.play("EVENT_VAMP_BITE");
                        this.imageEventText.updateBodyText(ACCEPT_BODY);
                        AbstractDungeon.player.decreaseMaxHealth(this.maxHpLoss);
                        this.replaceAttacks();
                        logMetricObtainCardsLoseMapHP("Vampires", "Became a vampire", this.bites, this.maxHpLoss);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        return;
                    case 1:
                        if (this.hasVial)
                        {
                            CardCrawlGame.sound.play("EVENT_VAMP_BITE");
                            this.imageEventText.updateBodyText(GIVE_VIAL);
                            AbstractDungeon.player.loseRelic("Blood Vial");

                            AbstractCard c = new Special_VampireBlood();
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                            logMetricObtainCard("Vampires", "Became a vampire (Vial)", c);

                            this.screenNum = 1;
                            this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                            this.imageEventText.clearRemainingOptions();
                            return;
                        }
                    default:
                        logMetricIgnored("Vampires");
                        this.imageEventText.updateBodyText(EXIT_BODY);
                        this.screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        return;
                }
            case 1:
                this.openMap();
                break;
            default:
                this.openMap();
        }
    }

    private void replaceAttacks()
    {
        ArrayList<AbstractCard> masterDeck = AbstractDungeon.player.masterDeck.group;

        int i;
        for (i = masterDeck.size() - 1; i >= 0; --i)
        {
            AbstractCard card = masterDeck.get(i);
            if (card.tags.contains(CardTags.STARTER_STRIKE))
            {
                AbstractDungeon.player.masterDeck.removeCard(card);
            }
        }

        for (i = 0; i < 4; ++i)
        {
            AbstractCard c = new Special_Bite();
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
            this.bites.add(c.cardID);
        }
    }

    static
    {
        final EventStrings original = GR.GetEventStrings(com.megacrit.cardcrawl.events.city.Vampires.ID);
        eventStrings = GR.GetEventStrings(ID);
        NAME = original.NAME;
        DESCRIPTIONS = original.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
        ACCEPT_BODY = DESCRIPTIONS[2];
        EXIT_BODY = DESCRIPTIONS[3];
        GIVE_VIAL = DESCRIPTIONS[4];
    }
}
