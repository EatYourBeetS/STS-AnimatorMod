package eatyourbeets.cards.animatorClassic.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animatorClassic.special.Vesta_Elixir;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.effects.VestaElixirEffects.AnimatorClassicVestaElixirEffects;
import eatyourbeets.cards.effects.VestaElixirEffects.VestaElixirEffect;
import eatyourbeets.cards.effects.VestaElixirEffects.VestaElixirEffect_CompleteFaster;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

import java.util.ArrayList;

public class Vesta extends AnimatorClassicCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(Vesta.class).SetSeriesFromClassPackage()
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();
    static
    {
        DATA.AddPreview(new Vesta_Elixir(), false);
    }

    private int timer;
    private Vesta_Elixir elixir;

    public Vesta()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, -1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        AnimatorClassicVestaElixirEffects.BeginCreateElixir((Vesta) this.makeStatEquivalentCopy(), this.upgraded);
    }

    public void ResearchElixir(Vesta_Elixir elixir)
    {
        this.elixir = elixir;
        this.timer = magicNumber;

        ArrayList<VestaElixirEffect> effects = new ArrayList<>();
        for (VestaElixirEffect effect : elixir.effects)
        {
            if (effect instanceof VestaElixirEffect_CompleteFaster)
            {
                this.timer -= 1;
            }
            else
            {
                effects.add(effect);
            }
        }

        this.elixir.ApplyEffects(effects);

        CombatStats.onStartOfTurnPostDraw.Subscribe(this);
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (timer > 0)
        {
            timer -= 1;
        }
        else
        {
            GameEffects.Queue.ShowCardBriefly(this);
            GameActions.Bottom.MakeCardInHand(elixir);

            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }
}