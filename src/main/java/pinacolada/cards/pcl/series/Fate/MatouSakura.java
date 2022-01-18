package pinacolada.cards.pcl.series.Fate;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.actions.orbs.RemoveOrb;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.status.Crystallize;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.powers.common.RippledPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class MatouSakura extends PCLCard
{
    public static final PCLCardData DATA = Register(MatouSakura.class)
            .SetSkill(2, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage().PostInitialize(data ->
                    data.AddPreview(new Crystallize(), false));

    public MatouSakura()
    {
        super(DATA);

        Initialize(0, 2, 2, 15);
        SetUpgrade(0, 4, 0, 0);

        SetAffinity_Dark(1, 0, 2);
        SetAffinity_Blue(1, 0, 1);

        SetExhaust(true);

        SetAffinityRequirement(PCLAffinity.Light, 5);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetAttackTarget(PCLGameUtilities.HasOrb(Dark.ORB_ID) ? eatyourbeets.cards.base.EYBCardTarget.Normal : eatyourbeets.cards.base.EYBCardTarget.None);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.Callback(m, (enemy, __) ->
        {
           final AbstractOrb orb = PCLGameUtilities.GetFirstOrb(Dark.ORB_ID);
           if (orb != null)
           {
               PCLGameEffects.Queue.RoomTint(Color.BLACK, 0.8F);
               PCLGameEffects.Queue.BorderLongFlash(new Color(1.0F, 0.0F, 1.0F, 0.7F));
               PCLGameEffects.Queue.Attack(player, enemy, AttackEffects.DARK, 1.1f, 1.2f);
               PCLActions.Bottom.Add(new RemoveOrb(orb));
               PCLActions.Bottom.ApplyRippled(TargetHelper.Normal(enemy), orb.evokeAmount);
               PCLActions.Bottom.SFX(SFX.ORB_DARK_EVOKE, 0.85f, 0.9f);
           }
           else
           {
               PCLActions.Bottom.ChannelOrbs(Dark::new, magicNumber);

           }
        });
        PCLActions.Bottom.MakeCardInHand(new Crystallize());

        if (TrySpendAffinity(PCLAffinity.Light))
        {
            PCLActions.Bottom.AddPowerEffectEnemyBonus(RippledPower.POWER_ID, secondaryValue);
        }
    }
}