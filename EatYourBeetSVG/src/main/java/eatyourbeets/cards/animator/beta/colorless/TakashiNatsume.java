package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.curse.Curse_Delusion;
import eatyourbeets.cards.animator.beta.curse.Curse_Depression;
import eatyourbeets.cards.animator.beta.curse.Curse_JunTormented;
import eatyourbeets.cards.animator.beta.special.TakashiNatsume_Circle;
import eatyourbeets.cards.animator.curse.Curse_Greed;
import eatyourbeets.cards.animator.curse.Curse_GriefSeed;
import eatyourbeets.cards.animator.curse.Curse_Nutcracker;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class TakashiNatsume extends AnimatorCard
{
    public static final EYBCardData DATA = Register(TakashiNatsume.class).SetSkill(1, CardRarity.RARE, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.NatsumeYuujinchou)
            .PostInitialize(data -> data.AddPreview(new TakashiNatsume_Circle(), false));

    public TakashiNatsume()
    {
        super(DATA);

        Initialize(0, 0, 1 );
        SetUpgrade(0, 0, 0 );

        SetAffinity_Light(2);
        SetAffinity_Water(2);
        SetExhaust(true);
        SetHealing(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SelectFromPile(name, magicNumber, player.hand, player.discardPile)
                .SetOptions(false, true)
                .SetFilter(c -> c.type == CardType.CURSE)
                .AddCallback(cards -> {
                    if (cards.size() > 0) {
                        for (AbstractCard c : cards) {
                            GameActions.Bottom.Exhaust(c).AddCallback(this::CreateCurseEffect);
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
        else if (c instanceof Decay) {
            circle.ChangeForm(TakashiNatsume_Circle.Form.Decay);
        }
        else if (c instanceof Doubt) {
            circle.ChangeForm(TakashiNatsume_Circle.Form.Doubt);
        }
        else if (c instanceof Necronomicurse) {
            circle.ChangeForm(TakashiNatsume_Circle.Form.Necronomicurse);
        }
        else if (c instanceof Normality) {
            circle.ChangeForm(TakashiNatsume_Circle.Form.Normality);
        }
        else if (c instanceof Pain) {
            circle.ChangeForm(TakashiNatsume_Circle.Form.Pain);
        }
        else if (c instanceof Regret) {
            circle.ChangeForm(TakashiNatsume_Circle.Form.Regret);
        }
        else if (c instanceof Shame) {
            circle.ChangeForm(TakashiNatsume_Circle.Form.Shame);
        }

        GameActions.Bottom.MakeCardInHand(circle);
    }
}