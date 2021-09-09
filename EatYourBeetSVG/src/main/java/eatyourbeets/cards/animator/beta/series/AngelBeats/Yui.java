package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.GirlDeMo;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Yui extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Yui.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new GirlDeMo(), false));

    public Yui()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Light(2, 0, 0);
        SetHarmonic(true);
        SetExhaust(true);
        AfterLifeMod.Add(this);

        SetAffinityRequirement(Affinity.General, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.MakeCardInHand(AffinityToken.GetCard(Affinity.Light)).SetUpgrade(upgraded, false).AddCallback(
                () -> {
                    GameActions.Bottom.Motivate(CheckAffinity(Affinity.General) ? secondaryValue + magicNumber : secondaryValue);
                }
        );

        if (CombatStats.ControlPile.Contains(this))
        {
            GameActions.Bottom.MakeCardInDrawPile(new GirlDeMo());
        }
    }
}