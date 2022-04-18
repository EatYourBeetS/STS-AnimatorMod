package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.vfx.megacritCopy.SmokeBombEffect2;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

import java.util.ArrayList;

public class GoshiNorito extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GoshiNorito.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public GoshiNorito()
    {
        super(DATA);

        Initialize(0,3, 2);
        SetCostUpgrade(-1);

        SetAffinity_Red(1);
        SetAffinity_Blue(2, 0, 1);

        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return magicNumber > 1 ? super.GetBlockInfo().AddMultiplier(magicNumber) : super.GetBlockInfo();
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        for (EnemyIntent intent : GameUtilities.GetIntents())
        {
            intent.AddWeak();
        }
    }

    @Override
    protected float GetInitialBlock()
    {
        final ArrayList<String> debuffs = new ArrayList<>();
        for (AbstractMonster m : GameUtilities.GetEnemies(true))
        {
            for (AbstractPower p : m.powers)
            {
                if (p.type == AbstractPower.PowerType.DEBUFF && !debuffs.contains(p.ID))
                {
                    debuffs.add(p.ID);
                }
            }
        }

        return super.GetInitialBlock() + debuffs.size();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final float stepX = Settings.WIDTH / 8f; // 0 (1 2 3 4 5 6 7) 8
        GameActions.Bottom.VFX(new SmokeBombEffect2(stepX * 4, p.hb.cY), 0.02f);
        for (int i = 1; i <= 3; i++)
        {
            final float y = p.hb.cY + (p.hb.height * ((i % 2 == 0) ? +0.25f : -0.25f));
            GameActions.Bottom.VFX(new SmokeBombEffect2(stepX * (4 - i), y), 0.02f);
            GameActions.Bottom.VFX(new SmokeBombEffect2(stepX * (4 + i), y), 0.02f);
        }

        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.GainBlock(block).SetVFX(i > 0, i > 0);
        }

        GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(), 1);
    }
}