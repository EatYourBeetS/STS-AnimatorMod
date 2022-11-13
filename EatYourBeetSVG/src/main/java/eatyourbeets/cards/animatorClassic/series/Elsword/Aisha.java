package eatyourbeets.cards.animatorClassic.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Aisha extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Aisha.class).SetSeriesFromClassPackage().SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental);

    public Aisha()
    {
        super(DATA);

        Initialize(2, 0, 0);
        SetUpgrade(0, 0, 1);
        SetScaling(1, 0, 0);


        SetSpellcaster();
    }

    @Override
    protected void OnUpgrade()
    {
        upgradedDamage = true;
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        GameUtilities.IncreaseMagicNumber(this, player.filledOrbCount(), true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.FIRE)
            .SetVFX(true, false)
            .SetDamageEffect(enemy ->
            {
                GameEffects.List.Add(VFX.SmallLaser(player.hb, enemy.hb, Color.PURPLE));
                return GameEffects.List.Add(VFX.SmallLaser(player.hb, enemy.hb, Color.VIOLET)).duration * 0.1f;
            });
        }
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle && CombatStats.TryActivateLimited(cardID))
        {
            GameEffects.List.ShowCopy(this);
            GameActions.Bottom.GainOrbSlots(1);
        }
    }
}