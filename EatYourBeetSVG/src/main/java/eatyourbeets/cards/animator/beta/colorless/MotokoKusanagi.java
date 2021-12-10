package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
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
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Silver(1, 0, 0);
        SetAffinity_Green(1, 0, 1);

        SetRicochet(BASE_RICOCHET, 0, this::OnCooldownCompleted);

        SetExhaust(true);
        SetHitCount(2);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);
        this.baseCooldownValue = this.cooldownValue = Math.max(1, BASE_RICOCHET - Math.floorDiv(player.gold, GOLD_THRESHOLD));
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        GameActions.Bottom.GainBlur(magicNumber);
        GameActions.Bottom.GainSupportDamage(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.GUNSHOT);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.MoveCard(this, player.exhaustPile, player.drawPile)
                .ShowEffect(true, false);
    }
}