package pinacolada.cards.pcl.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class Keqing extends PCLCard
{
    public static final PCLCardData DATA = Register(Keqing.class).SetAttack(2, CardRarity.UNCOMMON, PCLAttackType.Piercing).SetSeriesFromClassPackage();

    public Keqing()
    {
        super(DATA);

        Initialize(2, 0, 2, 2);
        SetUpgrade(1, 0, 1);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Green(1, 0, 2);
        SetAffinity_Dark(0, 0, 1);


        SetRicochet(4, 0, this::OnCooldownCompleted);

        SetHaste(true);
        SetExhaust(true);
        SetHitCount(3);
    }

    @Override
    public void triggerWhenDrawn() {
        if (this.hasTag(HASTE)) {
            PCLActions.Top.ChannelOrb(new Lightning());
            PCLActions.Top.ApplyElectrified(TargetHelper.Enemies(), magicNumber);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.VFX(new DieDieDieEffect());
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.DAGGER);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        PCLActions.Bottom.MoveCard(this, player.exhaustPile, player.drawPile)
                .ShowEffect(true, true);
        PCLActions.Bottom.ModifyTag(this, HASTE, true);
    }

}