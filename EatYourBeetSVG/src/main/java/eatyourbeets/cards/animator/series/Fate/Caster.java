package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardEffectChoice;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Caster extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Caster.class)
            .SetSkill(1, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public Caster()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0, 0, 0, -1);

        SetAffinity_Water(2);
        SetAffinity_Dark(2);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null)
        {
            GameUtilities.GetIntent(m).AddStrength(-magicNumber);
        }
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetEvokeOrbCount(HasSynergy() ? 1 : 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ReduceStrength(m, magicNumber, false).SetStrengthGain(true);
        GameActions.Bottom.ApplyFrail(null, p, secondaryValue);
        GameActions.Bottom.RaiseDarkLevel(magicNumber);
        GameActions.Bottom.StackPower(TargetHelper.Player(), PowerHelper.Endurance, -1);

        if (info.IsSynergizing)
        {
            GameActions.Bottom.ChannelOrb(new Dark()).AddCallback(o -> {
                if (o.size() > 0) {
                    GameActions.Bottom.TriggerOrbPassive(o.get(0),1);
                }
            });
        }
    }
}