package pinacolada.cards.pcl.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.MatouShinji_CommandSpell;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import java.util.HashSet;

public class MatouShinji extends PCLCard
{
    private static final HashSet<CardType> cardTypes = new HashSet<>();

    public static final PCLCardData DATA = Register(MatouShinji.class)
            .SetSkill(1, CardRarity.COMMON, PCLCardTarget.Random)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new MatouShinji_CommandSpell(), false));

    public MatouShinji()
    {
        super(DATA);

        Initialize(0, 3, 4, 4);
        SetUpgrade(0, 0, 2);

        SetAffinity_Orange(1);
        SetAffinity_Dark(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.Dark, 5);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block)
        .SetVFX(true, true);

        PCLActions.Bottom.Callback(() ->
        {
            final AbstractMonster enemy = PCLGameUtilities.GetRandomEnemy(true);
            if (PCLGameUtilities.IsValidTarget(enemy))
            {
                PCLActions.Top.ApplyPoison(player, enemy, magicNumber);
                PCLActions.Top.VFX(new PotionBounceEffect(player.hb.cX, player.hb.cY, enemy.hb.cX, enemy.hb.cY), 0.3f);
            }
        });

        if (info.CanActivateSemiLimited && TrySpendAffinity(PCLAffinity.Dark) && info.TryActivateSemiLimited())
        {
            PCLActions.Bottom.DealDamageAtEndOfTurn(player, player, secondaryValue, AttackEffects.DARK);
            PCLActions.Bottom.MakeCardInHand(new MatouShinji_CommandSpell());
        }
    }
}
