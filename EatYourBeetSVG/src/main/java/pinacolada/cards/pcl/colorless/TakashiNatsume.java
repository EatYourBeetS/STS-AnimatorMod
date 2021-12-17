package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.curse.*;
import pinacolada.cards.pcl.special.TakashiNatsume_Circle;
import pinacolada.utilities.PCLActions;

public class TakashiNatsume extends PCLCard
{
    public static final PCLCardData DATA = Register(TakashiNatsume.class).SetSkill(0, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.NatsumeYuujinchou)
            .PostInitialize(data -> data.AddPreview(new TakashiNatsume_Circle(), false));

    public TakashiNatsume()
    {
        super(DATA);

        Initialize(0, 0, 1 );
        SetUpgrade(0, 0, 0 );

        SetAffinity_Light(1);
        SetAffinity_Blue(1);
        SetExhaust(true);
        SetHealing(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.SelectFromPile(name, magicNumber, player.hand, player.discardPile)
                .SetOptions(false, true)
                .SetFilter(c -> c.type == CardType.CURSE)
                .AddCallback(cards -> {
                    if (cards.size() > 0) {
                        for (AbstractCard c : cards) {
                            PCLActions.Bottom.Exhaust(c).AddCallback(this::CreateCurseEffect);
                        }
                    }
                });
    }

    private void CreateCurseEffect(AbstractCard c) {
        TakashiNatsume_Circle circle = new TakashiNatsume_Circle();
        if (upgraded) {
            circle.upgrade();
        }
        if (c instanceof Curse_Delusion) {
            circle.ChangeForm(TakashiNatsume_Circle.Form.Curse_Delusion);
        }
        else if (c instanceof Curse_Depression) {
            circle.ChangeForm(TakashiNatsume_Circle.Form.Curse_Depression);
        }
        else if (c instanceof Curse_GriefSeed) {
            circle.ChangeForm(TakashiNatsume_Circle.Form.Curse_GriefSeed);
        }
        else if (c instanceof Curse_Greed) {
            circle.ChangeForm(TakashiNatsume_Circle.Form.Curse_Greed);
        }
        else if (c instanceof Curse_JunTormented) {
            circle.ChangeForm(TakashiNatsume_Circle.Form.Curse_JunTormented);
        }
        else if (c instanceof Curse_Nutcracker) {
            circle.ChangeForm(TakashiNatsume_Circle.Form.Curse_Nutcracker);
        }
        else if (c instanceof Curse_Slumber) {
            circle.ChangeForm(TakashiNatsume_Circle.Form.Shame);
        }
        else if (c instanceof Curse_Decay || c instanceof Decay) {
            circle.ChangeForm(TakashiNatsume_Circle.Form.Decay);
        }
        else if (c instanceof Curse_Doubt || c instanceof Doubt) {
            circle.ChangeForm(TakashiNatsume_Circle.Form.Doubt);
        }
        else if (c instanceof Necronomicurse) {
            circle.ChangeForm(TakashiNatsume_Circle.Form.Necronomicurse);
        }
        else if (c instanceof Curse_Normality || c instanceof Normality) {
            circle.ChangeForm(TakashiNatsume_Circle.Form.Normality);
        }
        else if (c instanceof Curse_Pain || c instanceof Pain) {
            circle.ChangeForm(TakashiNatsume_Circle.Form.Pain);
        }
        else if (c instanceof Curse_Regret || c instanceof Regret) {
            circle.ChangeForm(TakashiNatsume_Circle.Form.Regret);
        }
        else if (c instanceof Curse_Shame || c instanceof Shame) {
            circle.ChangeForm(TakashiNatsume_Circle.Form.Shame);
        }
        //TODO slumber inverts impaired
        //TODO eclipse recovers HP
        //TODO destroyer damages only enemies
        //TODO witches restore magical girl form

        PCLActions.Bottom.MakeCardInHand(circle);
    }
}