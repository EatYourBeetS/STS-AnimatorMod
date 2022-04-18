package eatyourbeets.cards.animator.series.MadokaMagica;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.curse.special.Curse_GriefSeed;
import eatyourbeets.cards.animator.special.MamiTomoe_Candeloro;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.ui.common.EYBCardPopupActions;
import eatyourbeets.utilities.GameActions;

public class MamiTomoe extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MamiTomoe.class)
            .SetAttack(2, CardRarity.COMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPopupAction(new EYBCardPopupActions.MadokaMagica_Witch(MamiTomoe_Candeloro.DATA));
                data.AddPreview(new MamiTomoe_Candeloro(), true);
                data.AddPreview(new Curse_GriefSeed(), false);
            });

    public MamiTomoe()
    {
        super(DATA);

        Initialize(7, 0, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(2, 0, 1);
        SetAffinity_Light(2);

        SetCardPreview(c -> c.costForTurn == 0);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.ObtainAffinityToken(Affinity.Light, false);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT)
        .SetVFXColor(Color.GOLD).SetSoundPitch(0.6f, 0.6f);
        GameActions.Bottom.Draw(magicNumber)
        .SetFilter(c -> c.costForTurn == 0, false);

        if (info.IsSynergizing)
        {
            GameActions.Bottom.ObtainAffinityToken(Affinity.Light, false);
        }
    }
}
