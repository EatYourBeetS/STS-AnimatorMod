package eatyourbeets.cards.animator.colorless.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.ModifyIntent;
import eatyourbeets.utilities.TargetHelper;

import java.util.ArrayList;
import java.util.List;

public class KotoriKanbe extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KotoriKanbe.class).SetSkill(1, CardRarity.RARE).SetColor(CardColor.COLORLESS);
    public static final int HP_HEAL_THRESHOLD = 30;

    public KotoriKanbe()
    {
        super(DATA);

        Initialize(0, 0, 3, 4);

        SetEthereal(true);
        SetExhaust(true);
        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public String GetRawDescription()
    {
        return super.GetRawDescription(HP_HEAL_THRESHOLD);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnHoveringTarget(AbstractMonster mo)
    {
        int healAmt = mo.maxHealth - mo.currentHealth;
        int stacks = Math.floorDiv(healAmt, magicNumber);

        if (stacks > 0)
        {
            List<ModifyIntent> modifyintentList = new ArrayList<>();
            modifyintentList.add(new ModifyIntent(ModifyIntent.ModifyIntentType.Weak, 1));

            if (healAmt >= HP_HEAL_THRESHOLD && !CombatStats.HasActivatedLimited(cardID))
            {
                modifyintentList.add(new ModifyIntent(ModifyIntent.ModifyIntentType.Shackles, secondaryValue));
            }

            GameUtilities.ModifyIntentsPreview(TargetHelper.All(), modifyintentList);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int heal = m.maxHealth - m.currentHealth;
        int stacks = Math.floorDiv(heal, magicNumber);

        GameActions.Bottom.Heal(p, m, heal);

        if (stacks > 0)
        {
            GameActions.Bottom.ApplyWeak(p, m, stacks);
            GameActions.Bottom.ApplyVulnerable(p, m, stacks);

            if (heal >= HP_HEAL_THRESHOLD && CombatStats.TryActivateLimited(cardID))
            {
                GameActions.Bottom.ReduceStrength(m, secondaryValue, false);
            }
        }
    }
}