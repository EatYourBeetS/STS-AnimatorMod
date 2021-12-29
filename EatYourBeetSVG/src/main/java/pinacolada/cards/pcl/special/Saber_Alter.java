package pinacolada.cards.pcl.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.series.Fate.Saber;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Saber_Alter extends PCLCard
{
    public static final PCLCardData DATA = Register(Saber_Alter.class)
            .SetAttack(3, CardRarity.SPECIAL, PCLAttackType.Dark, eatyourbeets.cards.base.EYBCardTarget.ALL)
            .SetSeries(Saber.DATA.Series);

    public Saber_Alter()
    {
        super(DATA);

        Initialize(10, 0, 10);
        SetUpgrade(10, 0, 0);

        SetAffinity_Red(1);
        SetAffinity_Dark(1, 0, 1);
        SetAffinity_Blue(1, 0, 8);

        SetEthereal(true);
        SetPurge(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.VFX(new BorderLongFlashEffect(Color.PURPLE));
        PCLActions.Bottom.VFX(VFX.ShockWave(p.hb, Color.PURPLE), 0.4f).SetRealtime(true);

        PCLActions.Bottom.ApplyShackles(TargetHelper.Enemies(), secondaryValue).AddCallback(() -> {
            for (AbstractMonster mo : GameUtilities.GetEnemies(true)) {
                int debuffs = mo.powers != null ? mo.powers.size() : 0;
                Dark d = new Dark();
                PCLGameUtilities.ModifyOrbFocus(d, debuffs, true, true);
                PCLActions.Bottom.ChannelOrb(d);
            }

            PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.NONE).forEach(d -> d
                    .SetDamageEffect((c, __) -> PCLGameEffects.List.Add(VFX.VerticalImpact(c.hb).SetColor(Color.PURPLE))));
        });
    }
}