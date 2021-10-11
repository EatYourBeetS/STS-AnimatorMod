package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class MotokoKusanagi extends AnimatorCard
{
    public static final int GOLD_THRESHOLD = 150;
    public static final int BASE_RICOCHET = 4;

    public static final EYBCardData DATA = Register(MotokoKusanagi.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Ranged).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GhostInTheShell);

    public MotokoKusanagi()
    {
        super(DATA);

        Initialize(3, 0, 2);
        SetUpgrade(2, 0, 0);
        SetAffinity_Earth(1, 0, 0);
        SetAffinity_Water(1, 0, 0);
        SetAffinity_Air(1, 0, 1);

        SetRicochet(BASE_RICOCHET, 0, this::OnCooldownCompleted);

        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        this.baseCooldownValue = this.cooldownValue = Math.max(1, BASE_RICOCHET - Math.floorDiv(player.gold, GOLD_THRESHOLD));
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        GameActions.Bottom.GainBlur(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < magicNumber; i++) {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT);
        }
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.MoveCard(this, player.exhaustPile, player.drawPile)
                .ShowEffect(true, false);
    }
}