package eatyourbeets.cards.animator.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.CalmStance;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Elsword extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Elsword.class)
            .SetAttack(2, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Elsword()
    {
        super(DATA);

        Initialize(11, 0, 1);
        SetUpgrade(3,  0, 1);

        SetEvokeOrbCount(1);
        SetAffinity_Fire(2);

        SetProtagonist(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateLimited(this.cardID)) {
            GameActions.Bottom.ChangeStance(CalmStance.STANCE_ID);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL).SetVFXColor(Color.RED);

        GameActions.Bottom.ChannelOrb(new Fire());
    }
}